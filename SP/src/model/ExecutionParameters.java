package model;

public class ExecutionParameters {
	private int numberOfTransactions;
	private int numberOfDataInsertsInTransaction;
	private int intervalBetweenTransactions;
	private int materializedViewRefreshTransactionLimit;
	private int numberOfTasks;
	private TaskType taskType;
	private ConnectionParameters connectionParameters;

	public int getNumberOfTransactions() {
		return numberOfTransactions;
	}

	public void setNumberOfTransactions(int numberOfTransactions) {
		this.numberOfTransactions = numberOfTransactions;
	}

	public int getNumberOfDataInsertsInTransaction() {
		return numberOfDataInsertsInTransaction;
	}

	public void setNumberOfDataInsertsInTransaction(
			int numberOfDataInsertsInTransaction) {
		this.numberOfDataInsertsInTransaction = numberOfDataInsertsInTransaction;
	}

	public int getIntervalBetweenTransactions() {
		return intervalBetweenTransactions;
	}

	public void setIntervalBetweenTransactions(int intervalBetweenTransactions) {
		this.intervalBetweenTransactions = intervalBetweenTransactions;
	}

	public void setConnectionParameters(
			ConnectionParameters connectionParameters) {
		this.connectionParameters = connectionParameters;

	}

	public ConnectionParameters getConnectionParameters() {
		return connectionParameters;
	}

	public TaskType getTaskType() {
		return taskType;
	}

	public void setTaskType(TaskType taskType) {
		this.taskType = taskType;
	}

	public int getNumberOfTasks() {
		return numberOfTasks;
	}

	public void setNumberOfTasks(int numberOfTasks) {
		this.numberOfTasks = numberOfTasks;
	}

	public int getMaterializedViewRefreshTransactionLimit() {
		return materializedViewRefreshTransactionLimit;
	}

	public void setMaterializedViewRefreshTransactionLimit(int materializedViewRefreshTransactionLimit) {
		this.materializedViewRefreshTransactionLimit = materializedViewRefreshTransactionLimit;
	}

}
