package view.taskwindow;

import java.awt.BorderLayout;

import javax.swing.JDialog;

import model.ExecutionParameters;

import org.jfree.chart.ChartPanel;

import controller.TaskController;

public abstract class TaskWindow extends JDialog {

	private static final long serialVersionUID = 3104866895681375162L;
	protected final TaskController controller;
	protected final ExecutionParameters executionParameters;

	public TaskWindow(TaskController controller,
			ExecutionParameters executionParameters) {
		this.controller = controller;
		this.executionParameters = executionParameters;
		createView();
	}

	private void createView() {
		setLayout(new BorderLayout());
		add(createChart(), BorderLayout.CENTER);
		setSize(500, 300);
		setVisible(true);
	}

	protected abstract ChartPanel createChart();

	public abstract void updateChart(long transactionExecutionTime,
			int taskNumber);

	public abstract void resetCounters();
}
