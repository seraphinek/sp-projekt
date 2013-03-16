package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import view.TaskWindow;

public class ExecutionTask extends SwingWorker<Void, Void> {

	private final ExecutionParameters executionParameters;
	private final TaskWindow taskWindow;
	private Connection connection;

	public ExecutionTask(ExecutionParameters executionParameters,
			TaskWindow taskWindow) {
		this.executionParameters = executionParameters;
		this.taskWindow = taskWindow;

		try {
			connection = ConnectionFactory.createConnection(executionParameters
					.getConnectionParameters());
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,
					"Couldn't connect to database !");
			taskWindow.dispose();
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Couldn't find JDBC driver !");
			taskWindow.dispose();
		}

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
						start = System.currentTimeMillis();
						for (int j = 0; j < executionParameters
								.getNumberOfDataInsertsInTransaction(); j++) {
							PreparedStatement statement = null;
							String query = "SELECT * FROM h_nation";
							try {
								statement = connection.prepareStatement(query);
								statement.executeQuery();
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
