package main;

import javax.swing.SwingUtilities;

import controller.MainWindowController;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				MainWindowController mainController = new MainWindowController();
			}
		});
	}
}
