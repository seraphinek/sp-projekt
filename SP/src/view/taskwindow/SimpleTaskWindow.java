package view.taskwindow;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.Random;

import model.ExecutionParameters;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import utils.ComponentUtils;
import controller.TaskController;

public class SimpleTaskWindow extends TaskWindow {

	private static final long serialVersionUID = -768907304400779219L;

	private XYSeries averageTimeSeries;
	private XYDataset averageTimeDataSet;
	private IntervalXYDataset[] executionTimeDataSet;
	private XYSeries[] executionTimeDataSeries;
	private int numberOfResultsForEachTask[];
	private int[] numberOfResultsInTime;
	private double[] averageTimeForAllTasksInTime;

	private JFreeChart chart;

	public SimpleTaskWindow(TaskController controller,
			ExecutionParameters executionParameters) {
		super(controller, executionParameters);
	}

	@Override
	protected ChartPanel createChart() {
		Random random = new Random(System.currentTimeMillis());
		numberOfResultsForEachTask = new int[executionParameters
				.getNumberOfTasks()];
		numberOfResultsInTime = new int[executionParameters
				.getNumberOfTransactions()];
		averageTimeForAllTasksInTime = new double[executionParameters
				.getNumberOfTransactions()];
		executionTimeDataSeries = new XYSeries[executionParameters
				.getNumberOfTasks()];
		executionTimeDataSet = new IntervalXYDataset[executionParameters
				.getNumberOfTasks()];
		averageTimeSeries = new XYSeries("Average time");
		averageTimeDataSet = new XYSeriesCollection(averageTimeSeries);
		chart = ChartFactory.createXYBarChart("Execution chart",
				"Transaction count", false, "Time", executionTimeDataSet[0],
				PlotOrientation.VERTICAL, true, true, false);
		XYPlot plot = chart.getXYPlot();
		plot.setDataset(0, averageTimeDataSet);
		StandardXYItemRenderer averageTimeRenderer = new StandardXYItemRenderer();
		averageTimeRenderer.setSeriesPaint(0, Color.red);
		averageTimeRenderer.setSeriesStroke(0, new BasicStroke(2));
		plot.setRenderer(0, averageTimeRenderer);

		for (int i = 1; i < executionParameters.getNumberOfTasks() + 1; i++) {
			executionTimeDataSeries[i - 1] = new XYSeries("Task " + i);
			executionTimeDataSet[i - 1] = new XYSeriesCollection(
					executionTimeDataSeries[i - 1]);
			plot.setDataset(i, executionTimeDataSet[i - 1]);
			XYAreaRenderer executionTimeRenderer = new XYAreaRenderer();
			executionTimeRenderer.setSeriesPaint(
					0,
					new Color(random.nextFloat(), random.nextFloat(), random
							.nextFloat(), random.nextFloat()));
			plot.setRenderer(i, executionTimeRenderer);
		}

		NumberAxis numberAxis = (NumberAxis) plot.getDomainAxis();
		numberAxis.setTickMarkPaint(Color.green);
		numberAxis.setStandardTickUnits(ComponentUtils.prepareChartUnits());
		return new ChartPanel(chart);
	}

	@Override
	public void updateChart(long transactionExecutionTime, int taskNumber) {
		int currentResult = numberOfResultsForEachTask[taskNumber]++;
		executionTimeDataSeries[taskNumber].add(
				numberOfResultsForEachTask[taskNumber],
				transactionExecutionTime);
		int resultsBefore = numberOfResultsInTime[currentResult];
		double averageBefore = averageTimeForAllTasksInTime[currentResult];
		double newAverage = (averageBefore * resultsBefore + transactionExecutionTime)
				/ ++numberOfResultsInTime[currentResult];
		averageTimeForAllTasksInTime[currentResult] = newAverage;
		if (numberOfResultsInTime[currentResult] > 1) {
			averageTimeSeries.update(new Integer(currentResult + 1),
					new Double(newAverage));
		} else {
			averageTimeSeries.add(currentResult + 1, newAverage);
		}
	}

	@Override
	public void resetCounters() {
		// TODO Auto-generated method stub

	}
}
