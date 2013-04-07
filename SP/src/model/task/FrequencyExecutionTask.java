package model.task;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import model.ConnectionFactory;
import model.ExecutionParameters;
import view.taskwindow.TaskWindow;

public class FrequencyExecutionTask extends ExecutionTask {

	public FrequencyExecutionTask(ExecutionParameters executionParameters,
			TaskWindow taskWindow, int taskId) throws ClassNotFoundException,
			SQLException {
		super(executionParameters, taskWindow, taskId);
		setTriggerLimitsInDatabase();
	}

	public FrequencyExecutionTask(ExecutionParameters executionParameters,
			TaskWindow taskWindow, int taskId, Connection connection)
			throws ClassNotFoundException, SQLException {
		super(executionParameters, taskWindow, taskId, connection);
	}

	@Override
	protected void executionLoop(final String[] orderInserts,
			final Map<Integer, String[]> lineItemsInserts) {

		for (int i = 0; i < executionParameters.getNumberOfTransactions() + 1; i++) {
			final int x = i;

			final SwingWorker<Long, Void> swingWorker = new SwingWorker<Long, Void>() {

				private long start;
				private long result;

				@Override
				protected Long doInBackground() throws Exception {
					Statement statement = connection.createStatement();

					start = System.currentTimeMillis();
					for (int j = 0; j < executionParameters
							.getNumberOfDataInsertsInTransaction(); j++) {

						try {
							statement.addBatch(orderInserts[j]);
							for (String lineItemInsert : lineItemsInserts
									.get(j)) {
								statement.addBatch(lineItemInsert);
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					statement.executeBatch();
					connection.commit();
					statement.close();

					result = System.currentTimeMillis() - start;
					return result;
				}

				@Override
				protected void done() {
					if (x >= 1) {
						try {
							get();
							taskWindow.updateChart(result, taskId);
							summaryTime += result;
							latch.countDown();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			};

			Timer timer = new Timer();
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					swingWorker.execute();
				}
			}, i * executionParameters.getIntervalBetweenTransactions());
		}
	}

	@Override
	protected void done() {
		super.done();

		if (summaryTime / executionParameters.getNumberOfTransactions() < executionParameters
				.getIntervalBetweenTransactions() * 1.5) {
			taskWindow.resetCounters();
			executionParameters
					.setIntervalBetweenTransactions((int) (executionParameters
							.getIntervalBetweenTransactions() * 0.9));
			System.out.println("Zlecam kolejne zadanie o id: " + (taskId + 1)
					+ " oraz interwale: "
					+ executionParameters.getIntervalBetweenTransactions());
			ExecutionTask executionTask;
			try {
				executionTask = new FrequencyExecutionTask(executionParameters,
						taskWindow, taskId + 1, ConnectionFactory.connection);
				executionTask.execute();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			closeConnection();
			JOptionPane.showMessageDialog(
					taskWindow,
					"Upper bound reached for "
							+ executionParameters.getNumberOfTransactions()
							+ " transactions / "
							+ executionParameters
									.getIntervalBetweenTransactions() + "ms");
		}
	}

	protected void setTriggerLimitsInDatabase() throws SQLException {
		Statement statement = connection.createStatement();
		statement.addBatch("update transactionsLimit set limit="
				+ executionParameters
						.getMaterializedViewRefreshTransactionLimit());
		statement.addBatch("update insertsPerTransactionLimit set limit="
				+ executionParameters.getNumberOfDataInsertsInTransaction());
		statement.executeBatch();
		connection.commit();
		statement.close();
	}
}
