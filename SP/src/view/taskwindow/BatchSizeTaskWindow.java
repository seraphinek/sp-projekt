package view.taskwindow;

import java.awt.Color;
import java.text.NumberFormat;

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
import view.ExtendedChartPanel;
import controller.TaskController;

public class BatchSizeTaskWindow extends TaskWindow {
	private static final long serialVersionUID = -768907304400779219L;
	private int numberOfResults = 0;
	private double currentAverage = 0;
	XYSeries averageExecutionTimeSeries;

	public BatchSizeTaskWindow(TaskController controller,
			ExecutionParameters executionParameters) {
		super(controller, executionParameters);
	}

	@Override
	protected ChartPanel createChart() {
		averageExecutionTimeSeries = new XYSeries("Average execution delay");
		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(averageExecutionTimeSeries);
		final JFreeChart chart = ChartFactory.createXYLineChart(
				"Data insertion batch size benchmark",
				"Number of inserts in batch", "Average execution delay",
				dataset, PlotOrientation.VERTICAL, true, true, false);

		chart.setBackgroundPaint(Color.white);

		final XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);

		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMaximumFractionDigits(2);

		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setBaseItemLabelsVisible(true);
		renderer.setSeriesItemLabelsVisible(0, true);
		renderer.setSeriesVisible(1, false);
		plot.setRenderer(renderer);

		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(ComponentUtils.prepareChartUnits());

		return new ExtendedChartPanel(chart);
	}

	@Override
	public void updateChart(long transactionExecutionTime, int taskNumber) {
		int currentSubResult = numberOfResults
				% executionParameters.getNumberOfTransactions();
		double newAverage = (currentAverage * currentSubResult + (transactionExecutionTime - executionParameters
				.getIntervalBetweenTransactions())) / (currentSubResult + 1);
		System.out.println("ID:" + currentSubResult + ",ET:"
				+ transactionExecutionTime + ",OA:" + currentAverage + ",NA:"
				+ newAverage);
		currentAverage = newAverage;
		if (currentSubResult > 0) {
			averageExecutionTimeSeries.update(
					new Integer(executionParameters
							.getNumberOfDataInsertsInTransaction()),
					new Double(newAverage));
		} else {
			averageExecutionTimeSeries.add(
					executionParameters.getNumberOfDataInsertsInTransaction(),
					transactionExecutionTime);

		}
		numberOfResults++;
	}

	@Override
	public void resetCounters() {
		numberOfResults = 0;
		currentAverage = 0;
	}

}
