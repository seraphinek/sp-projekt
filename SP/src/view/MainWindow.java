package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import model.DatabaseType;
import model.TaskType;
import controller.MainWindowController;

public class MainWindow extends JFrame implements ActionListener {

	private static final int FIELD_HEIGHT = 20;
	private static final int FIELD_WIDTH = 200;
	private static final int LABEL_WIDTH = 280;

	private static final long serialVersionUID = 3172688540921699213L;

	private JSpinner numberOfTransactions;
	private JSpinner numberOfDataInsertsInTransaction;
	private JTextField warehousePortNumber;
	private JSpinner intervalBetweenCommits;
	private JSpinner numberOfTasks;
	private JButton defaultButton;
	private JButton executeTaskButton;
	private JButton exitButton;
	private JButton aboutButton;

	private final MainWindowController controller;
	private JTextField warehouseServerAddress;
	private JTextField warehouseDatabaseName;
	private JTextField warehouseUserName;
	private JComboBox<DatabaseType> warehouseDatabaseType;
	private JComboBox<TaskType> executionTaskType;
	private JPasswordField warehouseUserPassword;

	public MainWindow(MainWindowController mainWindowController) {
		this.controller = mainWindowController;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Data Warehouse Tester");
	}

	public void createView() {
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setResizable(false);
		add(createTitlePanel());
		add(createParametersPanel());
		add(createButtonsPanel());
		setVisible(true);
		pack();
	}

	private JPanel createButtonsPanel() {
		JPanel buttonsPanel = new JPanel();
		defaultButton = new JButton("Default values");
		defaultButton.addActionListener(controller
				.getDefaultParametersActionListener());
		executeTaskButton = new JButton("Execute task");
		executeTaskButton.addActionListener(controller
				.getExecuteTaskActionListener());
		aboutButton = new JButton("About...");
		aboutButton.addActionListener(this);
		exitButton = new JButton("Exit");
		exitButton.addActionListener(this);
		buttonsPanel.add(defaultButton);
		buttonsPanel.add(executeTaskButton);
		buttonsPanel.add(exitButton);
		buttonsPanel.add(aboutButton);
		return buttonsPanel;
	}

	private JPanel createParametersPanel() {
		JPanel parametersPanel = new JPanel();
		parametersPanel.setLayout(new BoxLayout(parametersPanel,
				BoxLayout.Y_AXIS));
		prepareWarehouseParametersFields(parametersPanel);
		prepareNumberOfTransactionsParametersFields(parametersPanel);
		prepareNumberOfDataInsertsInTranactionParametersFields(parametersPanel);
		prepareBreakBetweenCommitsParametersFields(parametersPanel);
		prepareNumberOfTasksParametersFields(parametersPanel);
		prepareExecutionTaskTypeParametersFields(parametersPanel);
		return parametersPanel;
	}

	private void prepareNumberOfTasksParametersFields(JPanel paramsPanel) {
		JPanel numberOfTasksPanel = new JPanel();
		JLabel numberOfTasksLabel = new JLabel("Number of parallel tasks:");
		numberOfTasksLabel.setPreferredSize(new Dimension(LABEL_WIDTH,
				FIELD_HEIGHT));
		numberOfTasksLabel.setLabelFor(numberOfTasks);
		numberOfTasksPanel.add(numberOfTasksLabel);
		numberOfTasks = new JSpinner(getSpinnerNumberModel());
		numberOfTasks
				.setPreferredSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));
		numberOfTasksPanel.add(numberOfTasks);
		paramsPanel.add(numberOfTasksPanel);
	}

	private void prepareExecutionTaskTypeParametersFields(JPanel paramsPanel) {
		JPanel executionTaskTypePanel = new JPanel();
		JLabel executionTaskTypeLabel = new JLabel("Type of task:");
		executionTaskTypeLabel.setPreferredSize(new Dimension(LABEL_WIDTH,
				FIELD_HEIGHT));
		executionTaskTypePanel.add(executionTaskTypeLabel);
		executionTaskType = new JComboBox<TaskType>(TaskType.values());
		executionTaskType.setPreferredSize(new Dimension(FIELD_WIDTH,
				FIELD_HEIGHT));
		executionTaskTypePanel.add(executionTaskType);
		paramsPanel.add(executionTaskTypePanel);
	}

	private void prepareBreakBetweenCommitsParametersFields(
			JPanel parametersPanel) {
		JPanel intervalBetweenCommitsPanel = new JPanel();
		JLabel intervalBetweenCommitsLabel = new JLabel(
				"Interval between transaction commits (in ms):");
		intervalBetweenCommitsLabel.setPreferredSize(new Dimension(LABEL_WIDTH,
				FIELD_HEIGHT));
		intervalBetweenCommitsPanel.add(intervalBetweenCommitsLabel);
		intervalBetweenCommits = new JSpinner(getSpinnerNumberModel());
		intervalBetweenCommits.setPreferredSize(new Dimension(FIELD_WIDTH,
				FIELD_HEIGHT));
		intervalBetweenCommitsPanel.add(intervalBetweenCommits);
		parametersPanel.add(intervalBetweenCommitsPanel);
	}

	private void prepareNumberOfDataInsertsInTranactionParametersFields(
			JPanel paramsPanel) {
		JPanel numberOfDataInsertsInTransactionPanel = new JPanel();
		JLabel numberOfDataInsertsInTransactionLabel = new JLabel(
				"Number of data inserts:");
		numberOfDataInsertsInTransactionLabel.setPreferredSize(new Dimension(
				LABEL_WIDTH, FIELD_HEIGHT));
		numberOfDataInsertsInTransactionPanel
				.add(numberOfDataInsertsInTransactionLabel);
		numberOfDataInsertsInTransaction = new JSpinner(getSpinnerNumberModel());
		numberOfDataInsertsInTransaction.setPreferredSize(new Dimension(
				FIELD_WIDTH, FIELD_HEIGHT));
		numberOfDataInsertsInTransactionPanel
				.add(numberOfDataInsertsInTransaction);
		paramsPanel.add(numberOfDataInsertsInTransactionPanel);
	}

	private void prepareNumberOfTransactionsParametersFields(JPanel paramsPanel) {
		JPanel numberOfTransactionsPanel = new JPanel();
		JLabel numberOfTransactionsLabel = new JLabel("Number of transactions:");
		numberOfTransactionsLabel.setPreferredSize(new Dimension(LABEL_WIDTH,
				FIELD_HEIGHT));
		numberOfTransactionsLabel.setLabelFor(numberOfTransactions);
		numberOfTransactionsPanel.add(numberOfTransactionsLabel);
		numberOfTransactions = new JSpinner(getSpinnerNumberModel());
		numberOfTransactions.setPreferredSize(new Dimension(FIELD_WIDTH,
				FIELD_HEIGHT));
		numberOfTransactionsPanel.add(numberOfTransactions);
		paramsPanel.add(numberOfTransactionsPanel);
	}

	private void prepareWarehouseParametersFields(JPanel paramsPanel) {
		JPanel warehousePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		warehousePanel.setPreferredSize(new Dimension(500, 100));
		createWarehouseAddressFields(warehousePanel);
		createWarehousePortNumberFields(warehousePanel);
		createWarehouseDatabaseNameFields(warehousePanel);
		createDatabaseTypeFields(warehousePanel);
		createWarehouseUserNameFields(warehousePanel);
		createWarehouseUserPasswordFields(warehousePanel);
		paramsPanel.add(warehousePanel);
	}

	private void createDatabaseTypeFields(JPanel warehousePanel) {
		JLabel warehouseDatabaseTypeLabel = new JLabel("Database type:");
		warehouseDatabaseTypeLabel.setLabelFor(warehouseUserPassword);
		warehouseDatabaseTypeLabel.setPreferredSize(new Dimension(90,
				FIELD_HEIGHT));
		warehousePanel.add(warehouseDatabaseTypeLabel);
		warehouseDatabaseType = new JComboBox<DatabaseType>(
				DatabaseType.values());
		warehouseDatabaseType
				.setPreferredSize(new Dimension(100, FIELD_HEIGHT));
		warehousePanel.add(warehouseDatabaseType);
	}

	private void createWarehouseUserPasswordFields(JPanel warehousePanel) {
		JLabel warehouseUserPasswordLabel = new JLabel("Password:");
		warehouseUserPasswordLabel.setLabelFor(warehouseUserPassword);
		warehouseUserPasswordLabel.setPreferredSize(new Dimension(90,
				FIELD_HEIGHT));
		warehousePanel.add(warehouseUserPasswordLabel);
		warehouseUserPassword = new JPasswordField();
		warehouseUserPassword
				.setPreferredSize(new Dimension(100, FIELD_HEIGHT));
		warehousePanel.add(warehouseUserPassword);
	}

	private void createWarehouseUserNameFields(JPanel warehousePanel) {
		JLabel warehouseUserNameLabel = new JLabel("User name:");
		warehouseUserNameLabel.setLabelFor(warehouseUserName);
		warehouseUserNameLabel
				.setPreferredSize(new Dimension(100, FIELD_HEIGHT));
		warehousePanel.add(warehouseUserNameLabel);
		warehouseUserName = new JTextField();
		warehouseUserName.setPreferredSize(new Dimension(180, FIELD_HEIGHT));
		warehousePanel.add(warehouseUserName);
	}

	private void createWarehouseDatabaseNameFields(JPanel warehousePanel) {
		JLabel warehouseDbNameLabel = new JLabel("Database name:");
		warehouseDbNameLabel.setLabelFor(warehouseDatabaseName);
		warehouseDbNameLabel.setPreferredSize(new Dimension(100, FIELD_HEIGHT));
		warehousePanel.add(warehouseDbNameLabel);
		warehouseDatabaseName = new JTextField();
		warehouseDatabaseName
				.setPreferredSize(new Dimension(180, FIELD_HEIGHT));
		warehousePanel.add(warehouseDatabaseName);
	}

	private void createWarehousePortNumberFields(JPanel warehousePanel) {
		JLabel warehousePortNumberLabel = new JLabel("Port number:");
		warehousePortNumberLabel.setLabelFor(warehousePortNumber);
		warehousePortNumberLabel.setPreferredSize(new Dimension(90,
				FIELD_HEIGHT));
		warehousePanel.add(warehousePortNumberLabel);
		warehousePortNumber = new JTextField();
		warehousePortNumber.setPreferredSize(new Dimension(100, FIELD_HEIGHT));
		warehousePanel.add(warehousePortNumber);
	}

	private void createWarehouseAddressFields(JPanel warehousePanel) {
		JLabel warehouseAddressLabel = new JLabel("Server address:");
		warehouseAddressLabel.setLabelFor(warehousePortNumber);
		warehouseAddressLabel
				.setPreferredSize(new Dimension(100, FIELD_HEIGHT));
		warehousePanel.add(warehouseAddressLabel);
		warehouseServerAddress = new JTextField();
		warehouseServerAddress
				.setPreferredSize(new Dimension(180, FIELD_HEIGHT));
		warehousePanel.add(warehouseServerAddress);
	}

	private JPanel createTitlePanel() {
		JPanel titlePanel = new JPanel(new BorderLayout());
		JLabel titleLabel = new JLabel("Data Warehouse Tester");
		titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 26));
		titlePanel.add(titleLabel);
		return titlePanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if (source.equals(aboutButton)) {
			controller.showAboutWindow();
			return;
		}

		if (source.equals(exitButton)) {
			controller.closeApplication();
			return;
		}
	}

	public SpinnerModel getSpinnerNumberModel() {
		return new SpinnerNumberModel(5, 1, Integer.MAX_VALUE, 1);
	}

	public JSpinner getNumberOfTransactions() {
		return numberOfTransactions;
	}

	public JSpinner getNumberOfDataInsertsInTransaction() {
		return numberOfDataInsertsInTransaction;
	}

	public JTextField getWarehousePortNumber() {
		return warehousePortNumber;
	}

	public JSpinner getIntervalBetweenCommits() {
		return intervalBetweenCommits;
	}

	public JTextField getWarehouseServerAddress() {
		return warehouseServerAddress;
	}

	public JTextField getWarehouseDatabaseName() {
		return warehouseDatabaseName;
	}

	public JTextField getWarehouseUserName() {
		return warehouseUserName;
	}

	public JPasswordField getWarehouseUserPassword() {
		return warehouseUserPassword;
	}

	public JComboBox<DatabaseType> getWarehouseDatabaseType() {
		return warehouseDatabaseType;
	}

	public JComboBox<TaskType> getExecutionTaskType() {
		return executionTaskType;
	}

	public JSpinner getNumberOfTasks() {
		return numberOfTasks;
	}

}
