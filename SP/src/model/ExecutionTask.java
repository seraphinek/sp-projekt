package model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import view.TaskWindow;

public class ExecutionTask extends SwingWorker<Void, Void> {

	private final ExecutionParameters executionParameters;
	private final TaskWindow taskWindow;
	private final Connection connection;
	private final DataFromFileReader fileUtils;

	public ExecutionTask(ExecutionParameters executionParameters,
			TaskWindow taskWindow) throws ClassNotFoundException, SQLException {
		this.executionParameters = executionParameters;
		this.taskWindow = taskWindow;

		this.fileUtils = DataFromFileReader.getInstance();
		this.connection = ConnectionFactory
				.createConnection(executionParameters.getConnectionParameters());

	}

	@Override
	protected Void doInBackground() throws Exception {
		try {
			for (int i = 0; i < executionParameters.getNumberOfTransactions(); i++) {
				SwingWorker<Long, Void> swingWorker = new SwingWorker<Long, Void>() {

					private long start;
					private long result;

					@Override
					protected Long doInBackground() throws Exception {

						String[] orderInserts = fileUtils
								.getOrderInserts(executionParameters
										.getNumberOfDataInsertsInTransaction());
						Map<Integer, String[]> lineItemsInserts = fileUtils
								.getLineItemsInsertsSet(executionParameters
										.getNumberOfDataInsertsInTransaction());

						start = System.currentTimeMillis();
						for (int j = 0; j < executionParameters
								.getNumberOfDataInsertsInTransaction(); j++) {
							java.sql.Statement statement = null;

							try {
								statement = connection.createStatement();
								statement.addBatch(orderInserts[j]);
								System.out.println(orderInserts[j]);
								for (String lineItemInsert : lineItemsInserts
										.get(j)) {
									statement.addBatch(lineItemInsert);
									System.out.println(lineItemInsert);
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
							taskWindow.updateChart(result);
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
