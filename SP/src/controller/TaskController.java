package controller;

import java.sql.SQLException;

import javax.swing.JOptionPane;

import model.ExecutionParameters;
import model.ExecutionTask;
import view.TaskWindow;

public class TaskController {

	private final TaskWindow taskWindow;
	private final ExecutionParameters executionParameters;
	private ExecutionTask executionTask;

	public TaskController(ExecutionParameters executionParameters) {
		this.executionParameters = executionParameters;
		this.taskWindow = new TaskWindow(this);
		try {
			this.executionTask = new ExecutionTask(executionParameters,
					taskWindow);
			this.executionTask.execute();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,
					"Couldn't connect to database !");
			taskWindow.dispose();
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Couldn't find JDBC driver !");
			taskWindow.dispose();
		}

	}

	public ExecutionParameters getExecutionParameters() {
		return executionParameters;
	}

}
