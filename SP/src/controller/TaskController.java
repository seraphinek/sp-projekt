package controller;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;

import model.ExecutionParameters;
import model.task.ExecutionTask;
import model.task.FrequencyExecutionTask;
import model.task.SimpleExecutionTask;
import view.taskwindow.FrequencyTaskWindow;
import view.taskwindow.SimpleTaskWindow;
import view.taskwindow.TaskWindow;

public class TaskController {

	private TaskWindow taskWindow;
	private ExecutionTask executionTask;

	public TaskController(ExecutionParameters executionParameters) {
		prepareTask(executionParameters);
	}

	public void prepareTask(ExecutionParameters executionParameters) {
		try {
			switch (executionParameters.getTaskType()) {
			case SIMPLE:
				this.taskWindow = new SimpleTaskWindow(this,
						executionParameters);
				for (int i = 0; i < executionParameters.getNumberOfTasks(); i++) {
					this.executionTask = new SimpleExecutionTask(
							executionParameters, taskWindow, i);
					this.executionTask.execute();
				}
				break;
			case FREQUENCY:
				this.taskWindow = new FrequencyTaskWindow(this,
						executionParameters);
				Long summaryTime = 0L;
				do {
					// executionParameters
					// .setIntervalBetweenTransactions(executionParameters
					// .getIntervalBetweenTransactions()
					// - executionParameters
					// .getIntervalBetweenTransactions()
					// / 10);
					System.out
							.println("Wystartowano nowe zadanie o czêstoœci :"
									+ executionParameters
											.getIntervalBetweenTransactions());
					this.executionTask = new FrequencyExecutionTask(
							executionParameters, taskWindow, 0);
					this.executionTask.execute();
					try {
						this.executionTask.get();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} while (summaryTime < executionParameters
						.getIntervalBetweenTransactions());
				System.out.println("Zakoñczono - krztusi siê dla : "
						+ executionParameters.getIntervalBetweenTransactions());
				break;
			default:
				break;
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,
					"Couldn't connect to database !");
			taskWindow.dispose();
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Couldn't find JDBC driver !");
			taskWindow.dispose();
		}
	}
}
