package controller;

import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import model.ExecutionParameters;
import model.task.SimpleExecutionTask;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import view.taskwindow.SimpleTaskWindow;
import view.taskwindow.TaskWindow;

public class TaskController {

	private TaskWindow taskWindow;
	private SwingWorker<Void, Void> executionTask;

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
				throw new NotImplementedException();
				// this.taskWindow = new FrequencyTaskWindow(this,
				// executionParameters);
				// this.executionTask = new FrequencyExecutionTask(
				// executionParameters, taskWindow, 0);
				// this.executionTask.execute();
				//
				// break;
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
