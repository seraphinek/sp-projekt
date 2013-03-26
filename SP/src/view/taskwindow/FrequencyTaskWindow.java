package view.taskwindow;

import java.awt.Color;

import model.ExecutionParameters;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import utils.ComponentUtils;
import controller.TaskController;

public class FrequencyTaskWindow extends TaskWindow {

	private static final long serialVersionUID = -768907304400779219L;
	private int numberOfResults = 0;
	private final double currentAverage = 0;
	XYSeries averageExecutionTimeSeries;

	public FrequencyTaskWindow(TaskController controller,
			ExecutionParameters executionParameters) {
		super(controller, executionParameters);
	}

	@Override
	protected ChartPanel createChart() {
		averageExecutionTimeSeries = new XYSeries("Average execution time");
		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(averageExecutionTimeSeries);

		final JFreeChart chart = ChartFactory.createXYLineChart(
				"Data insertion frequency benchmark",
				"Transactions per second", "Average execution time", dataset,
				PlotOrientation.VERTICAL, true, true, false);

		chart.setBackgroundPaint(Color.white);

		final XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);

		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		plot.setRenderer(renderer);

		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(ComponentUtils.prepareChartUnits());

		NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
		domainAxis.setStandardTickUnits(ComponentUtils.prepareChartUnits());

		return new ChartPanel(chart);
	}

	@Override
	public void updateChart(long transactionExecutionTime, int taskNumber) {
		int currentSubResult = numberOfResults++
				% executionParameters.getNumberOfTransactions();
		double newAverage = (currentAverage * currentSubResult - 1 + transactionExecutionTime)
				/ (currentSubResult + 1);
		System.out.println("update chart " + numberOfResults + " average "
				+ newAverage);
		if (currentSubResult > 0) {
			averageExecutionTimeSeries.update(
					new Double((double) executionParameters
							.getNumberOfTransactions()
							* 1000
							/ executionParameters
									.getIntervalBetweenTransactions()),
					new Double(newAverage));
		} else {

			averageExecutionTimeSeries.add(
					(double) executionParameters.getNumberOfTransactions()
							* 1000
							/ executionParameters
									.getIntervalBetweenTransactions(),
					transactionExecutionTime);

		}
	}
}
