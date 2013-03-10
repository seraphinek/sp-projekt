package model;

import view.TaskWindow;

public class ExecutionTask extends Thread {

	private final ExecutionParameters executionParameters;
	private final TaskWindow taskWindow;

	public ExecutionTask(ExecutionParameters executionParameters,
			TaskWindow taskWindow) {
		this.executionParameters = executionParameters;
		this.taskWindow = taskWindow;
	}

	@Override
	public void run() {
		super.run();

		for (int i = 0; i < executionParameters.getNumberOfTransactions(); i++) {
			long start = System.currentTimeMillis();
			for (int j = 0; j < executionParameters
					.getNumberOfDataInsertsInTransaction(); j++) {

			}
			long stop = System.currentTimeMillis();
			taskWindow.updateChart(stop - start);
		}
	}
}
