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
		this.taskWindow = new TaskWindow();
		this.executionTask = new ExecutionTask(executionParameters, taskWindow);
		this.executionTask.run();
	}

	public ExecutionParameters getExecutionParameters() {
		return executionParameters;
	}

}
