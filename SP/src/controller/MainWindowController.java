package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import model.ExecutionParameters;
import utils.ComponentUtils;
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

	private final ActionListener defaultParametersActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					mainWindow.getNumberOfDataInsertsInTransaction()
							.setValue(5);
					mainWindow.getIntervalBetweenCommits().setValue(5);
					mainWindow.getNumberOfTransactions().setValue(15);
					mainWindow.getWarehouseServerAddress().setText(
							"djmaciej.sytes.net");
					mainWindow.getWarehousePortNumber().setText("3306");
					mainWindow.getWarehouseDatabaseName().setText("janusz");
					mainWindow.getWarehouseUserName().setText("janusz");
					mainWindow.getWarehouseUserPassword().setText("janusz");
				}
			});
		}
	};

	private final ActionListener executeTaskActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					if (validateParameters()) {
						new TaskController(gatherExecutionParameters());
					} else {
						JOptionPane.showMessageDialog(mainWindow,
								"Invalid parameters !");
					}
				};

			});
		}
	};

	private ExecutionParameters gatherExecutionParameters() {
		ExecutionParameters parameters = new ExecutionParameters();
		parameters.setIntervalBetweenTransactions((Integer) mainWindow
				.getIntervalBetweenCommits().getValue());
		parameters.setNumberOfDataInsertsInTransaction((Integer) mainWindow
				.getNumberOfDataInsertsInTransaction().getValue());
		parameters.setNumberOfTransactions((Integer) mainWindow
				.getNumberOfTransactions().getValue());
		parameters.setDbName(mainWindow.getWarehouseDatabaseName().getText());
		parameters.setServerName(mainWindow.getWarehouseServerAddress()
				.getText());
		parameters.setPortNumber(Integer.decode(mainWindow
				.getWarehousePortNumber().getText()));
		parameters.setUserName(mainWindow.getWarehouseUserName().getText());
		parameters.setPassword(new String(mainWindow.getWarehouseUserPassword()
				.getPassword()));
		return parameters;
	}

	private boolean validateParameters() {
		if (ComponentUtils.isEmptyOrNull(mainWindow.getWarehouseServerAddress()
				.getText())) {
			return false;
		}
		if (ComponentUtils.isEmptyOrNull(mainWindow.getWarehousePortNumber()
				.getText())) {
			return false;
		}
		if (ComponentUtils.isEmptyOrNull(mainWindow.getWarehouseDatabaseName()
				.getText())) {
			return false;
		}
		if (ComponentUtils.isEmptyOrNull(mainWindow.getWarehouseUserName()
				.getText())) {
			return false;
		}
		if (ComponentUtils.isEmptyOrNull(new String(mainWindow
				.getWarehouseUserPassword().getPassword()))) {
			return false;
		}
		return true;
	}

	public ActionListener getExecuteTaskActionListener() {
		return executeTaskActionListener;
	}

	public ActionListener getDefaultParametersActionListener() {
		return defaultParametersActionListener;
	}

}
