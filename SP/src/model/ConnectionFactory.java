package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
	public static Connection connection; 
	
	public static Connection createConnection(
			ConnectionParameters connectionParameters) throws SQLException,
			ClassNotFoundException {
		
		String connectionString = "";

		switch (connectionParameters.getDatabaseType()) {
		case ORACLE:
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connectionString = "jdbc:oracle:thin:@"
					+ connectionParameters.getServerName() + ":"
					+ connectionParameters.getPortNumber() + ":"
					+ connectionParameters.getDbName();
			break;
		case MYSQL:
			Class.forName("com.mysql.jdbc.Driver");
			connectionString = "jdbc:mysql://"
					+ connectionParameters.getServerName()
					+ ":"
					+ connectionParameters.getPortNumber()
					+ "/"
					+ connectionParameters.getDbName()
					+ "?user="
					+ connectionParameters.getUserName()
					+ "&password="
					+ connectionParameters.getUserPassword()
					+ "&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&failOverReadOnly=false&maxReconnects=10";
			break;
		default:
			break;
		}

		connection = DriverManager.getConnection(connectionString,
				connectionParameters.getUserName(),
				connectionParameters.getUserPassword());
		connection.setAutoCommit(false);

		return connection;
	}
	
}
