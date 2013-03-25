package model.task;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.swing.SwingWorker;

import model.ExecutionParameters;
import view.taskwindow.TaskWindow;

public class SimpleExecutionTask extends ExecutionTask {

	public SimpleExecutionTask(ExecutionParameters executionParameters,
			TaskWindow taskWindow, int taskId) throws ClassNotFoundException,
			SQLException {
		super(executionParameters, taskWindow, taskId);
	}

	@Override
	protected void executionLoop(final String[] orderInserts,
			final Map<Integer, String[]> lineItemsInserts) {
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
						latch.countDown();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			swingWorker.execute();
		}
	}

}
