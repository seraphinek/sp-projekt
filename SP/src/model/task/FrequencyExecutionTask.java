package model.task;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import model.ExecutionParameters;
import view.taskwindow.TaskWindow;

public class FrequencyExecutionTask extends ExecutionTask {

	public FrequencyExecutionTask(ExecutionParameters executionParameters,
			TaskWindow taskWindow, int taskId) throws ClassNotFoundException,
			SQLException {
		super(executionParameters, taskWindow, taskId);
	}

	@Override
	protected void executionLoop(final String[] orderInserts,
			final Map<Integer, String[]> lineItemsInserts) {
		for (int i = 0; i < executionParameters.getNumberOfTransactions() + 5; i++) {
			final int x = i;

			final SwingWorker<Long, Void> swingWorker = new SwingWorker<Long, Void>() {

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
					if (x >= 5) {
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
				.getIntervalBetweenTransactions()*1.5) {
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
						taskWindow, taskId + 1);
				executionTask.execute();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			JOptionPane.showMessageDialog(
					taskWindow,
					"Upper bound reached for "
							+ executionParameters.getNumberOfTransactions()
							+ " transactions / "
							+ executionParameters
									.getIntervalBetweenTransactions() + "ms");
		}
	}
}
