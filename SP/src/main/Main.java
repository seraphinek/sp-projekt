package main;

import javax.swing.SwingUtilities;

import model.DataFromFileReader;
import view.LoadingWindow;
import controller.MainWindowController;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final LoadingWindow loadingWindow = new LoadingWindow();

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				DataFromFileReader.getInstance();
				new MainWindowController();
				loadingWindow.dispose();
			};
		});

	}
}
