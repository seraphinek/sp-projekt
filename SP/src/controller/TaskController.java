package controller;

import model.ExecutionParameters;
import model.ExecutionTask;
import view.TaskWindow;

public class TaskController {

	private final TaskWindow taskWindow;
	private final ExecutionParameters executionParameters;
	private final ExecutionTask executionTask;

	public TaskController(ExecutionParameters executionParameters) {
		this.executionParameters = executionParameters;
		this.taskWindow = new TaskWindow(this);
		this.executionTask = new ExecutionTask(executionParameters, taskWindow);
		this.executionTask.execute();
	}

	public ExecutionParameters getExecutionParameters() {
		return executionParameters;
	}

}
