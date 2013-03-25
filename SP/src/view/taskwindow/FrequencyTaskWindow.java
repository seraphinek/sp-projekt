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

		// create the chart...
		final JFreeChart chart = ChartFactory.createXYLineChart(
				"Line Chart Demo 6", // chart title
				"Frequency of sql queries", // x axis label
				"Execution time", // y axis label
				dataset, // data
				PlotOrientation.VERTICAL, true, // include legend
				true, // tooltips
				false // urls
				);

		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
		chart.setBackgroundPaint(Color.white);

		// final StandardLegend legend = (StandardLegend) chart.getLegend();
		// legend.setDisplaySeriesShapes(true);

		// get a reference to the plot for further customisation...
		final XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(Color.lightGray);
		// plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);

		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		plot.setRenderer(renderer);

		// change the auto tick unit selection to integer units only...
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(ComponentUtils.prepareChartUnits());
		// OPTIONAL CUSTOMISATION COMPLETED.

		return new ChartPanel(chart);
	}

	@Override
	public void updateChart(long transactionExecutionTime, int taskNumber) {
		numberOfResults++;

		int currentSubResult = numberOfResults
				% executionParameters.getNumberOfTransactions() + 1;
		double newAverage = (currentAverage * currentSubResult - 1 + transactionExecutionTime)
				/ currentSubResult;
		// int currentSample = numberOfResults
		// / executionParameters.getNumberOfTransactions();
		System.out.println("update chart " + numberOfResults + " average "
				+ newAverage);
		// if (currentSubResult != 1) {
		// averageExecutionTimeSeries.update(new Integer(currentSample),
		// new Double(newAverage));
		// } else {
		averageExecutionTimeSeries.add(numberOfResults,
				transactionExecutionTime);
		// }
	}
}
