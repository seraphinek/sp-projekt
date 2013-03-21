package model.task;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import model.ConnectionFactory;
import model.DataFromFileReader;
import model.ExecutionParameters;
import view.taskwindow.TaskWindow;

public class FrequencyExecutionTask extends SwingWorker<Void, Void> {

	private final ExecutionParameters executionParameters;
	private final TaskWindow taskWindow;
	private final Connection connection;
	private final DataFromFileReader fileUtils;
	private final int taskId;

	public FrequencyExecutionTask(ExecutionParameters executionParameters,
			TaskWindow taskWindow, int taskId) throws ClassNotFoundException,
			SQLException {
		this.executionParameters = executionParameters;
		this.taskWindow = taskWindow;
		this.taskId = taskId;
		this.fileUtils = DataFromFileReader.getInstance();
		this.connection = ConnectionFactory
				.createConnection(executionParameters.getConnectionParameters());
	}

	@Override
	protected Void doInBackground() throws Exception {
		try {
			final String[] orderInserts = fileUtils
					.getOrderInserts(executionParameters
							.getNumberOfDataInsertsInTransaction());
			final Map<Integer, String[]> lineItemsInserts = fileUtils
					.getLineItemsInsertsSet(executionParameters
							.getNumberOfDataInsertsInTransaction());

			for (int i = 0; i < executionParameters.getNumberOfTransactions(); i++) {
				SwingWorker<Long, Void> swingWorker = new SwingWorker<Long, Void>() {

					private long start;
					private long result;

					@Override
					protected Long doInBackground() throws Exception {
						start = System.currentTimeMillis();
						for (int j = 0; j < executionParameters
								.getNumberOfDataInsertsInTransaction(); j++) {
							Statement statement = null;
							try {
								statement = connection.createStatement();
								statement.addBatch(orderInserts[j]);
								for (String lineItemInsert : lineItemsInserts
										.get(j)) {
									statement.addBatch(lineItemInsert);
								}
								statement.executeBatch();
								statement.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
						result = System.currentTimeMillis() - start;
						return result;
					}

					@Override
					protected void done() {
						try {
							get();
							taskWindow.updateChart(result, taskId);
						} catch (InterruptedException | ExecutionException e) {
							e.printStackTrace();
						}
					}
				};

				swingWorker.execute();
				while (!swingWorker.isDone())
					;
			}
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(taskWindow,
						"Problem with closing connection !");
			}
		}
		return null;
	}

}
