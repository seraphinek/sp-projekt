package view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JDialog;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.TickUnits;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import controller.TaskController;

public class TaskWindow extends JDialog {

	private static final long serialVersionUID = 3104866895681375162L;

	private IntervalXYDataset executionTimeDataSet;
	private final TaskController controller;
	private long numberOfResults;
	private double averageTime;
	private long summaryTime;
	private XYSeries executionTimeDataSeries;
	private XYSeries averageTimeSeries;
	private XYDataset averageTimeDataSet;

	public TaskWindow(TaskController controller) {
		this.controller = controller;
		createView();
	}

	private void createView() {
		setLayout(new BorderLayout());
		add(createChart(), BorderLayout.CENTER);
		setSize(500, 300);
		setVisible(true);
	}

	public void updateChart(long transactionExecutionTime) {
		numberOfResults++;
		summaryTime += transactionExecutionTime;
		averageTime = (double) summaryTime / (double) numberOfResults;
		executionTimeDataSeries.add(numberOfResults, transactionExecutionTime);
		averageTimeSeries.add(numberOfResults, averageTime);

	}

	private ChartPanel createChart() {
		executionTimeDataSeries = new XYSeries("Execution times");
		averageTimeSeries = new XYSeries("Average time");
		executionTimeDataSet = new XYSeriesCollection(executionTimeDataSeries);
		averageTimeDataSet = new XYSeriesCollection(averageTimeSeries);
		final JFreeChart chart = ChartFactory.createXYBarChart(
				"Execution chart", "Transaction count", false, "Time",
				executionTimeDataSet, PlotOrientation.VERTICAL, true, true,
				false);

		XYPlot plot = chart.getXYPlot();

		plot.setDataset(0, averageTimeDataSet);
		StandardXYItemRenderer averageTimeRenderer = new StandardXYItemRenderer();
		averageTimeRenderer.setSeriesPaint(0, Color.red);
		averageTimeRenderer.setSeriesStroke(0, new BasicStroke(2));
		plot.setRenderer(0, averageTimeRenderer);

		plot.setDataset(1, executionTimeDataSet);
		XYAreaRenderer executionTimeRenderer = new XYAreaRenderer();
		executionTimeRenderer.setSeriesPaint(0, new Color(0f, 1f, 0f, 0.2f));
		plot.setRenderer(1, executionTimeRenderer);

		NumberAxis numberAxis = (NumberAxis) plot.getDomainAxis();
		numberAxis.setTickMarkPaint(Color.green);
		final TickUnits standardUnits = new TickUnits();
		standardUnits.add(new NumberTickUnit(1));
		standardUnits.add(new NumberTickUnit(10));
		standardUnits.add(new NumberTickUnit(100));
		standardUnits.add(new NumberTickUnit(1000)); // Kilo
		standardUnits.add(new NumberTickUnit(10000));
		standardUnits.add(new NumberTickUnit(100000));
		standardUnits.add(new NumberTickUnit(1000000)); // Mega
		numberAxis.setStandardTickUnits(standardUnits);

		return new ChartPanel(chart);
	}
}
