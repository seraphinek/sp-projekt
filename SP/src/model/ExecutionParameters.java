package model;

public class ExecutionParameters {
	private int numberOfTransactions;
	private int numberOfDataInsertsInTransaction;
	private int intervalBetweenTransactions;
	private String serverName;
	private String dbName;
	private String userName;
	private String userPassword;
	private int portNumber;

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

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setPassword(String passwordName) {
		this.userPassword = passwordName;
	}

	public int getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}
}
