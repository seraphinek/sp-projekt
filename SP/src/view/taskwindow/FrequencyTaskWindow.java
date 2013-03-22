package view.taskwindow;

import java.util.Random;

import model.ExecutionParameters;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;

import controller.TaskController;

public class FrequencyTaskWindow extends TaskWindow {

	private static final long serialVersionUID = -768907304400779219L;

	public FrequencyTaskWindow(TaskController controller,
			ExecutionParameters executionParameters) {
		super(controller, executionParameters);
	}

	@Override
	protected ChartPanel createChart() {
		double[] value = new double[100];
		Random generator = new Random();
		for (int i = 1; i < 100; i++) {
			value[i] = generator.nextDouble();
		}
		int number = 10;
		HistogramDataset dataset = new HistogramDataset();
		dataset.setType(HistogramType.RELATIVE_FREQUENCY);
		dataset.addSeries("Histogram", value, number);
		String plotTitle = "Histogram";
		String xaxis = "number";
		String yaxis = "value";
		PlotOrientation orientation = PlotOrientation.VERTICAL;
		boolean show = false;
		boolean toolTips = false;
		boolean urls = false;
		JFreeChart chart = ChartFactory.createHistogram(plotTitle, xaxis,
				yaxis, dataset, orientation, show, toolTips, urls);
		return new ChartPanel(chart);
	}

	@Override
	public void updateChart(long transactionExecutionTime, int taskNumber) {
	}

}
