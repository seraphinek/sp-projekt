package model.task;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import model.ConnectionFactory;
import model.DataFromFileReader;
import model.ExecutionParameters;
import utils.Latch;
import view.taskwindow.TaskWindow;

public abstract class ExecutionTask extends SwingWorker<Long, Void> {

	protected final ExecutionParameters executionParameters;
	protected final TaskWindow taskWindow;
	protected final Connection connection;
	protected final DataFromFileReader fileUtils;
	protected final Random random = new Random(System.currentTimeMillis());
	protected int taskId;
	protected final Latch latch;
	protected long summaryTime;

	public ExecutionTask(ExecutionParameters executionParameters,
			TaskWindow taskWindow, int taskId) throws ClassNotFoundException,
			SQLException {
		this.executionParameters = executionParameters;
		this.taskWindow = taskWindow;
		this.taskId = taskId;
		this.latch = new Latch(executionParameters.getNumberOfTransactions());
		this.fileUtils = DataFromFileReader.getInstance();
		this.connection = ConnectionFactory
				.createConnection(executionParameters.getConnectionParameters());
	}

	public ExecutionTask(ExecutionParameters executionParameters,
			TaskWindow taskWindow, int taskId, Connection connection)
			throws ClassNotFoundException, SQLException {
		this.executionParameters = executionParameters;
		this.taskWindow = taskWindow;
		this.taskId = taskId;
		this.latch = new Latch(executionParameters.getNumberOfTransactions());
		this.fileUtils = DataFromFileReader.getInstance();
		this.connection = connection;
	}

	@Override
	protected Long doInBackground() throws Exception {
		try {
			final String[] orderInserts = fileUtils
					.getOrderInserts(executionParameters
							.getNumberOfDataInsertsInTransaction());
			final Map<Integer, String[]> lineItemsInserts = fileUtils
					.getLineItemsInsertsSet(executionParameters
							.getNumberOfDataInsertsInTransaction());
			executionLoop(orderInserts, lineItemsInserts);
		} finally {
			latch.awaitZero();
		}

		return summaryTime;
	}

	protected void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(taskWindow,
					"Problem with closing connection !");
		}
	}

	protected abstract void executionLoop(final String[] orderInserts,
			final Map<Integer, String[]> lineItemsInserts);
}
