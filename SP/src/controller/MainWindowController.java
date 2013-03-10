package controller;

import javax.swing.JOptionPane;

import model.ExecutionParameters;
import view.AboutWindow;
import view.MainWindow;

public class MainWindowController {
	private final MainWindow mainWindow;

	public MainWindowController() {
		this.mainWindow = new MainWindow(this);
		mainWindow.createView();
	}

	public void showAboutWindow() {
		new AboutWindow();
	}

	public void closeApplication() {
		System.exit(0);
	}

	public void setDefaultParameters() {
		mainWindow.getNumberOfDataInsertsInTransaction().setValue(5);
		mainWindow.getIntervalBetweenCommits().setValue(5);
		mainWindow.getNumberOfTransactions().setValue(5);
		mainWindow.getWarehouseAddress().setText("localhost:1234");

	}

	public void executeTask() {
		if (validateParameters()) {
			new TaskController(gatherExecutionParameters());
		} else {
			JOptionPane.showMessageDialog(mainWindow, "Invalid parameters !");
		}
	}

	private ExecutionParameters gatherExecutionParameters() {
		ExecutionParameters parameters = new ExecutionParameters();
		parameters.setIntervalBetweenTransactions((Integer) mainWindow
				.getIntervalBetweenCommits().getValue());
		parameters.setNumberOfDataInsertsInTransaction((Integer) mainWindow
				.getNumberOfDataInsertsInTransaction().getValue());
		parameters.setNumberOfTransactions((Integer) mainWindow
				.getNumberOfTransactions().getValue());
		parameters.setWarehouseAddress(mainWindow.getWarehouseAddress()
				.getText());
		return parameters;
	}

	private boolean validateParameters() {
		if (mainWindow.getWarehouseAddress().getText() == null
				|| "".equals(mainWindow.getWarehouseAddress().getText())) {
			return false;
		}
		return true;
	}

}
