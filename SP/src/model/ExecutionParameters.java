package model;

public class ExecutionParameters {
	private int numberOfTransactions;
	private int numberOfDataInsertsInTransaction;
	private int intervalBetweenTransactions;
	private String warehouseAddress;

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

	public String getWarehouseAddress() {
		return warehouseAddress;
	}

	public void setWarehouseAddress(String warehouseAddress) {
		this.warehouseAddress = warehouseAddress;
	}

}
