package view.taskwindow;

import model.ExecutionParameters;

import org.jfree.chart.ChartPanel;

import controller.TaskController;

public class FrequencyTaskWindow extends TaskWindow {

	private static final long serialVersionUID = -768907304400779219L;

	public FrequencyTaskWindow(TaskController controller,
			ExecutionParameters executionParameters) {
		super(controller, executionParameters);
	}

	@Override
	protected ChartPanel createChart() {
		return null;
	}

	@Override
	public void updateChart(long transactionExecutionTime, int taskNumber) {
	}

}
