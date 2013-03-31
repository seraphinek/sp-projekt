package model.task;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Map;

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
	protected final int taskId;
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

	@Override
	protected Long doInBackground() throws Exception {

		try {
			final String[] orderInserts = fileUtils
					.getOrderInserts(executionParameters
							.getNumberOfDataInsertsInTransaction());
			final Map<Integer, String[]> lineItemsInserts = fileUtils
					.getLineItemsInsertsSet(executionParameters
							.getNumberOfDataInsertsInTransaction());
			// tuneDatabase();
			executionLoop(orderInserts, lineItemsInserts);
		} finally {
			latch.awaitZero();
			try {
				connection.close();
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(taskWindow,
						"Problem with closing connection !");
			}
		}

		return summaryTime;
	}

	protected abstract void executionLoop(final String[] orderInserts,
			final Map<Integer, String[]> lineItemsInserts);

	@SuppressWarnings("unused")
	private void tuneDatabase() {
		System.out.println(new Date() + "|Preparing data for tuning");
		final String[] orderInserts = fileUtils.getOrderInserts(5);
		final Map<Integer, String[]> lineItemsInserts = fileUtils
				.getLineItemsInsertsSet(25);
		System.out.println(new Date()
				+ "|Data for tuning prepared, starting tuning...");
		for (int i = 0; i < 25; i++) {
			for (int j = 0; j < 5; j++) {
				Statement statement = null;
				try {
					statement = connection.createStatement();
					statement.addBatch(orderInserts[j]);
					for (String lineItemInsert : lineItemsInserts.get(j)) {
						statement.addBatch(lineItemInsert);
					}
					statement.executeBatch();
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println(new Date() + "|Tuning finished.");
	}

}
