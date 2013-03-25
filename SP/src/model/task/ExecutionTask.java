package model.task;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.swing.SwingWorker;

import model.DataFromFileReader;
import model.ExecutionParameters;
import utils.Latch;
import view.taskwindow.TaskWindow;

public abstract class ExecutionTask extends SwingWorker<Long, Void> {

	protected final ExecutionParameters executionParameters;
	protected final TaskWindow taskWindow;
	protected final Connection connection;
	protected final DataFromFileReader fileUtils;
	protected final int taskId;
	protected final Latch latch;

	public ExecutionTask(ExecutionParameters executionParameters,
			TaskWindow taskWindow, int taskId) throws ClassNotFoundException,
			SQLException {
		this.executionParameters = executionParameters;
		this.taskWindow = taskWindow;
		this.taskId = taskId;
		this.latch = new Latch(executionParameters.getNumberOfTransactions());
		this.fileUtils = DataFromFileReader.getInstance();
		this.connection = null;// ConnectionFactory.createConnection(executionParameters.getConnectionParameters());
	}

	@Override
	protected Long doInBackground() throws Exception {
		long startTime = System.currentTimeMillis();
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
			// try {
			// connection.close();
			// } catch (SQLException e) {
			// JOptionPane.showMessageDialog(taskWindow,
			// "Problem with closing connection !");
			// }
		}
		return System.currentTimeMillis() - startTime;
	}

	protected abstract void executionLoop(final String[] orderInserts,
			final Map<Integer, String[]> lineItemsInserts);
}
