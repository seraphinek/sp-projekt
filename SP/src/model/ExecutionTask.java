package model;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;


import utils.FileUtils;
import view.TaskWindow;

public class ExecutionTask extends SwingWorker<Void, Void> {

	private final ExecutionParameters executionParameters;
	private final TaskWindow taskWindow;
	private Connection connection;
	private FileUtils fileUtils;

	public ExecutionTask(ExecutionParameters executionParameters,
			TaskWindow taskWindow) {
		this.executionParameters = executionParameters;
		this.taskWindow = taskWindow;

		try {
			connection = ConnectionFactory.createConnection(executionParameters
					.getConnectionParameters());
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,
					"Couldn't connect to database !");
			taskWindow.dispose();
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Couldn't find JDBC driver !");
			taskWindow.dispose();
		}

	}

	@Override
	protected Void doInBackground() throws Exception {
		try {
			for (int i = 0; i < executionParameters.getNumberOfTransactions(); i++) {
				SwingWorker<Long, Void> swingWorker = new SwingWorker<Long, Void>() {

					private long start;
					private long result;

					@Override
					protected Long doInBackground() throws Exception {
						fileUtils = new FileUtils();
						ArrayList<String> orderInserts = getOrderInserts(executionParameters
								.getNumberOfDataInsertsInTransaction());
						Map<Integer, ArrayList<String>> lineItemsInsetrs = getLineItemsInsertsSet(executionParameters
								.getNumberOfDataInsertsInTransaction());
						
						start = System.currentTimeMillis();
						for (int j = 0; j < executionParameters
								.getNumberOfDataInsertsInTransaction(); j++) {
							java.sql.Statement statement = null;
							
							try {
								statement = connection.createStatement();
								
								statement.addBatch(orderInserts.get(j));
								for (String lineItemInsert : lineItemsInsetrs.get(j))
								{
									statement.addBatch(lineItemInsert);
								}
								
								statement.executeBatch();
								statement.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
							
						}
						
						result = System.currentTimeMillis() - start;
						return result;
					}

					@Override
					protected void done() {
						try {
							get();
							taskWindow.updateChart(result);
						} catch (InterruptedException | ExecutionException e) {
							e.printStackTrace();
						}
					}
				};

				swingWorker.execute();
				while (!swingWorker.isDone())
					;
			}
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(taskWindow,
						"Problem with closing connection !");
			}
		}
		return null;
	}
	
	protected HashMap<Integer, ArrayList<String>> getLineItemsInsertsSet(int numberOfOrders)
	{
		HashMap<Integer, ArrayList<String>> lineItemsInsertsSet = new HashMap<Integer, ArrayList<String>>();
		
		for (int i = 0; i < numberOfOrders; i++)
		{
			lineItemsInsertsSet.put(i, getLineItemsInserts());
		}
		
		return lineItemsInsertsSet;
	}
	
	protected ArrayList<String> getLineItemsInserts()
	{
		Random rand = new Random();
		ArrayList<String> inserts = new ArrayList<String>();
		
		for (int i = 0; i < rand.nextInt(7) + 1; i++)
		{
			inserts.add(getLineItemsInsert(i+1));
		}
		
		return inserts;
	}
	
	protected String getLineItemsInsert(int lineNumber)
	{
		String insertText = null;
		ArrayList<String> dataToInsert;
		Random rand = new Random();
		
		dataToInsert =  getLineParts(fileUtils.getParticularLineItem(rand.nextInt(598285)));
			
        insertText = "INSERT INTO H_LINEITEM( " +
                		"l_orderkey, " +
                		"l_partkey, " +
                		"l_suppkey, " +
                		"l_linenumber, " +
                		"l_quantity, " +
                		"l_extendedprice, " +
                		"l_discount, " +
                		"l_tax, " +
                		"l_returnflag, " +
                		"l_linestatus, " +
                		"l_shipdate, " +
                		"l_commitdate, " +
                		"l_receiptdate, " +
                		"l_shipinstruct, " +
                		"l_shipmode, " +
                		"l_comment) VALUES (" +
                		"order_seq.currval, " +
                		dataToInsert.get(1) + "," +
                		dataToInsert.get(2) + "," +
                		lineNumber + "," +
                		dataToInsert.get(4) + "," +
                		dataToInsert.get(5) + "," +
                		dataToInsert.get(6) + "," +
                		dataToInsert.get(7) + "," +
                		"'" + dataToInsert.get(8) + "'," +
                		"'" + dataToInsert.get(9) + "'," +
                		"to_date('" + dataToInsert.get(10) + "','YYYY-MM-DD')," +
                		"to_date('" + dataToInsert.get(11) + "','YYYY-MM-DD')," +
                		"to_date('" + dataToInsert.get(12) + "','YYYY-MM-DD')," +
                		"'" + dataToInsert.get(13) + "'," +
                		"'" + dataToInsert.get(14) + "'," +
                		"'" + dataToInsert.get(15) + "')";
		
		return insertText;
	}
	
	protected ArrayList<String> getOrderInserts(int numberOfDataInsertsInTransaction)
	{
		int dataLineNumber;
		Random rand = new Random();
		ArrayList<String> listOfInserts = new ArrayList<String>();
		
		for (int i = 0; i < numberOfDataInsertsInTransaction; i++)
		{
			dataLineNumber = rand.nextInt(180000);
			listOfInserts.add(getOrderInsert(dataLineNumber));
		}
		
		return listOfInserts;
	}
	
	protected String getOrderInsert(int dataLineNumber)
	{
		String insertText = null;
		ArrayList<String> dataToInsert;
	
		dataToInsert = getLineParts(fileUtils.getParticularOrder(dataLineNumber));
			
        insertText = "INSERT INTO H_ORDER( " +
            			"o_orderkey, " +
                		"o_custkey, " +
                		"o_orderstatus," +
                		"o_totalprice, " +
                		"o_orderdate, " +
                		"o_orderpriority, " +
                		"o_clerk, " +
                		"o_shippriority, " +
                		"o_comment) VALUES (" +
                		"order_seq.nextval," +
                		dataToInsert.get(1) + "," +
                		"'" + dataToInsert.get(2) + "'," +
                		dataToInsert.get(3) + "," +
                		"to_date('" + dataToInsert.get(4) + "','YYYY-MM-DD'), " +
                		"'" + dataToInsert.get(5) + "'," +
                		"'" + dataToInsert.get(6) + "'," +
                		dataToInsert.get(7) + "," +
                		"'" + dataToInsert.get(8) + "')";
		
		return insertText;
	}

	
	private ArrayList<String> getLineParts(String line)
	{
		ArrayList<String> lineParts = new ArrayList<String>(
				Arrays.asList(line.split("\\|"))); 
		
		return lineParts;
	}
}
