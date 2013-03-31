package view;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileFilter;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;

public class ExtendedChartPanel extends ChartPanel {

	private static final long serialVersionUID = 2441214162103826349L;

	public ExtendedChartPanel(JFreeChart chart) {
		super(chart);
	}

	@Override
	protected JPopupMenu createPopupMenu(boolean properties, boolean save,
			boolean print, boolean zoom) {
		JPopupMenu menu = super.createPopupMenu(properties, save, print, zoom);
		JMenuItem menuItem = new JMenuItem(exportDataToCsv());
		menuItem.setText("Export to CSV...");
		menu.add(menuItem);
		return menu;
	}

	@Override
	protected JPopupMenu createPopupMenu(boolean arg0, boolean arg1,
			boolean arg2, boolean arg3, boolean arg4) {
		JPopupMenu menu = super.createPopupMenu(arg0, arg1, arg2, arg3, arg4);
		JMenuItem menuItem = new JMenuItem(exportDataToCsv());
		menuItem.setText("Export to CSV...");
		menu.add(menuItem);
		return menu;
	}

	public Action exportDataToCsv() {
		return new Action() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new FileFilter() {

					@Override
					public String getDescription() {
						return "CSV File, *.csv";
					}

					@Override
					public boolean accept(File f) {
						if (!f.isDirectory()
								&& f.getAbsolutePath().toLowerCase()
										.endsWith(".csv")) {
							return true;
						}
						return false;
					}
				});
				if (fileChooser.showSaveDialog(ExtendedChartPanel.this) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					if (!file.getAbsolutePath().toLowerCase().endsWith(".csv")) {
						file = new File(file.getAbsolutePath() + ".csv");
					}
					Charset charset = Charset.forName("UTF-8");
					BufferedWriter writer = null;
					try {
						writer = Files.newBufferedWriter(file.toPath(),
								charset, StandardOpenOption.CREATE);
					} catch (IOException e) {
						e.printStackTrace();
						return;
					}
					XYPlot plot = getChart().getXYPlot();
					System.out.println("--- EXPORT TO CSV ---");
					int x = -1;
					while (true) {
						boolean stop = false;
						for (int i = 0; i < plot.getDatasetCount(); i++) {
							XYDataset dataset = plot.getDataset(i);
							for (int j = i > 0 ? 0 : -1; j < dataset
									.getSeriesCount(); j++) {
								String s = "";
								if (x < 0) {
									if (j < 0) {
										s = plot.getDomainAxis().getLabel();
									} else {
										s = (String) dataset.getSeriesKey(j);
									}
								} else {
									if (x < dataset.getItemCount(j < 0 ? 0 : j)) {
										if (j < 0) {
											s = dataset.getX(0, x).toString();
										} else {
											s = dataset.getY(j, x).toString();
										}
									} else {
										stop = true;
										break;
									}
								}

								if (i == plot.getDatasetCount() - 1
										&& j == dataset.getSeriesCount() - 1) {
									s += "\n";
								} else {
									s += ",";
								}

								try {
									System.out.print(s);
									writer.write(s, 0, s.length());
								} catch (IOException e) {
									e.printStackTrace();
									return;
								}
							}
						}
						if (stop == true) {
							try {
								writer.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
							break;
						}
						x++;
					}
				}
			}

			@Override
			public void addPropertyChangeListener(PropertyChangeListener arg0) {
			}

			@Override
			public Object getValue(String arg0) {
				return null;
			}

			@Override
			public boolean isEnabled() {
				return true;
			}

			@Override
			public void putValue(String arg0, Object arg1) {
			}

			@Override
			public void removePropertyChangeListener(PropertyChangeListener arg0) {
			}

			@Override
			public void setEnabled(boolean arg0) {
			}

		};
	}
}
