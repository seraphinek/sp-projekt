package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import controller.MainWindowController;

public class MainWindow extends JFrame implements ActionListener {

	private static final int FIELD_HEIGHT = 20;
	private static final int FIELD_WIDTH = 200;
	private static final int LABEL_WIDTH = 280;

	private static final long serialVersionUID = 3172688540921699213L;

	private JSpinner numberOfTransactions;
	private JSpinner numberOfDataInsertsInTransaction;
	private JTextField warehouseAddress;
	private JSpinner intervalBetweenCommits;
	private JButton defaultButton;
	private JButton executeTaskButton;
	private JButton exitButton;
	private JButton aboutButton;

	private final MainWindowController controller;

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
		defaultButton.addActionListener(this);
		executeTaskButton = new JButton("Execute task");
		executeTaskButton.addActionListener(this);
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
		return parametersPanel;
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
		JPanel warehousePanel = new JPanel();
		JLabel warehouseAddressLabel = new JLabel("Warehouse address:");
		warehouseAddressLabel.setLabelFor(warehouseAddress);
		warehouseAddressLabel.setPreferredSize(new Dimension(LABEL_WIDTH,
				FIELD_HEIGHT));
		warehousePanel.add(warehouseAddressLabel);
		warehouseAddress = new JTextField();
		warehouseAddress.setPreferredSize(new Dimension(FIELD_WIDTH,
				FIELD_HEIGHT));
		warehousePanel.add(warehouseAddress);
		paramsPanel.add(warehousePanel);
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

		if (source.equals(executeTaskButton)) {
			controller.executeTask();
			return;
		}

		if (source.equals(defaultButton)) {
			controller.setDefaultParameters();
			return;
		}

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

	public JTextField getWarehouseAddress() {
		return warehouseAddress;
	}

	public JSpinner getIntervalBetweenCommits() {
		return intervalBetweenCommits;
	}

}
