package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import model.ConnectionParameters;
import model.DatabaseType;
import model.ExecutionParameters;
import model.TaskType;
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
							.setValue(20);
					mainWindow.getIntervalBetweenCommits().setValue(300);
					mainWindow.getNumberOfTasks().setValue(1);
					mainWindow.getNumberOfTransactions().setValue(5);
					mainWindow.getWarehouseServerAddress().setText(
							"25.165.194.88");
					mainWindow.getWarehousePortNumber().setText("1521");
					mainWindow.getWarehouseDatabaseName().setText("orcl");
					mainWindow.getWarehouseUserName().setText("tpch");
					mainWindow.getWarehouseUserPassword().setText("tpch");
					mainWindow.getWarehouseDatabaseType().setSelectedItem(
							DatabaseType.ORACLE);
					mainWindow.getExecutionTaskType().setSelectedItem(
							TaskType.FREQUENCY);
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
		ExecutionParameters executionParameters = new ExecutionParameters();
		executionParameters.setIntervalBetweenTransactions((Integer) mainWindow
				.getIntervalBetweenCommits().getValue());
		executionParameters
				.setNumberOfDataInsertsInTransaction((Integer) mainWindow
						.getNumberOfDataInsertsInTransaction().getValue());
		executionParameters.setNumberOfTransactions((Integer) mainWindow
				.getNumberOfTransactions().getValue());
		executionParameters.setTaskType((TaskType) mainWindow
				.getExecutionTaskType().getSelectedItem());
		executionParameters.setNumberOfTasks((Integer) mainWindow
				.getNumberOfTasks().getValue());
		ConnectionParameters connectionParameters = new ConnectionParameters();
		connectionParameters.setDbName(mainWindow.getWarehouseDatabaseName()
				.getText());
		connectionParameters.setServerName(mainWindow
				.getWarehouseServerAddress().getText());
		connectionParameters.setPortNumber(Integer.decode(mainWindow
				.getWarehousePortNumber().getText()));
		connectionParameters.setUserName(mainWindow.getWarehouseUserName()
				.getText());
		connectionParameters.setPassword(new String(mainWindow
				.getWarehouseUserPassword().getPassword()));
		connectionParameters.setDatabaseType((DatabaseType) mainWindow
				.getWarehouseDatabaseType().getSelectedItem());
		executionParameters.setConnectionParameters(connectionParameters);
		return executionParameters;
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
