package model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class DataFromFileReader {

	private static final int LINEITEMS_IN_ORDER = 7;
	private volatile static DataFromFileReader instance;
	private final Random rand;
	private final byte[][] lineItems;
	private final byte[][] orders;
	private final int orderCount;
	private final int lineItemsCount;

	private DataFromFileReader() {
		this.rand = new Random(System.currentTimeMillis());
		this.lineItems = readLineItems();
		this.orders = readOrders();
		this.orderCount = orders.length;
		this.lineItemsCount = lineItems.length;
	}

	public static DataFromFileReader getInstance() {
		if (instance == null) {
			synchronized (DataFromFileReader.class) {
				if (instance == null) {
					instance = new DataFromFileReader();
				}
			}
		}
		return instance;
	}

	private byte[][] readLineItems() {
		ArrayList<byte[]> lineItems = new ArrayList<byte[]>();
		Scanner scanner = null;
		try {
			scanner = new Scanner(new FileReader(
					"InsertDataSets/lineitem.tbl.u0"));
			while (scanner.hasNextLine()) {
				lineItems.add(scanner.nextLine().getBytes("UTF-8"));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
		byte[][] lineItemsArray = new byte[lineItems.size()][];
		for (int i = 0; i < lineItems.size(); i++) {
			lineItemsArray[i] = lineItems.get(i);
		}
		return lineItemsArray;
	}

	private byte[][] readOrders() {
		ArrayList<byte[]> orders = new ArrayList<byte[]>();
		Scanner scanner = null;
		try {
			scanner = new Scanner(
					new FileReader("InsertDataSets/orders.tbl.u0"));
			while (scanner.hasNextLine()) {
				orders.add(scanner.nextLine().getBytes("UTF-8"));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
		byte[][] lineItemsArray = new byte[orders.size()][];
		for (int i = 0; i < orders.size(); i++) {
			lineItemsArray[i] = orders.get(i);
		}
		return lineItemsArray;
	}

	public String[] getOrderInserts(int numberOfDataInsertsInTransaction) {
		int dataLineNumber;
		String[] listOfInserts = new String[numberOfDataInsertsInTransaction];
		for (int i = 0; i < numberOfDataInsertsInTransaction; i++) {
			dataLineNumber = rand.nextInt(orderCount);
			listOfInserts[i] = getOrderInsert(dataLineNumber);
		}
		return listOfInserts;
	}

	public String getOrderInsert(int dataLineNumber) {
		String[] dataToInsert = getParticularOrder(dataLineNumber).split("\\|");
		String insertText = "INSERT INTO H_ORDER( " + "o_orderkey, "
				+ "o_custkey, " + "o_orderstatus," + "o_totalprice, "
				+ "o_orderdate, " + "o_orderpriority, " + "o_clerk, "
				+ "o_shippriority, " + "o_comment) VALUES ("
				+ "order_seq.nextval," + dataToInsert[1] + "," + "'"
				+ dataToInsert[2] + "'," + dataToInsert[3] + "," + "to_date('"
				+ dataToInsert[4] + "','YYYY-MM-DD'), " + "'" + dataToInsert[5]
				+ "'," + "'" + dataToInsert[6] + "'," + dataToInsert[7] + ","
				+ "'" + dataToInsert[8] + "')";
		return insertText;
	}

	protected String getLineItemsInsert(int lineNumber) {
		String[] dataToInsert = getParticularLineItem(
				rand.nextInt(lineItemsCount)).split("\\|");
		String insertText = "INSERT INTO H_LINEITEM( " + "l_orderkey, "
				+ "l_partkey, " + "l_suppkey, " + "l_linenumber, "
				+ "l_quantity, " + "l_extendedprice, " + "l_discount, "
				+ "l_tax, " + "l_returnflag, " + "l_linestatus, "
				+ "l_shipdate, " + "l_commitdate, " + "l_receiptdate, "
				+ "l_shipinstruct, " + "l_shipmode, " + "l_comment) VALUES ("
				+ "order_seq.currval, "
				+ dataToInsert[1]
				+ ","
				+ dataToInsert[2]
				+ ","
				+ lineNumber
				+ ","
				+ dataToInsert[4]
				+ ","
				+ dataToInsert[5]
				+ ","
				+ dataToInsert[6]
				+ ","
				+ dataToInsert[7]
				+ ","
				+ "'"
				+ dataToInsert[8]
				+ "',"
				+ "'"
				+ dataToInsert[9]
				+ "',"
				+ "to_date('"
				+ dataToInsert[10]
				+ "','YYYY-MM-DD'),"
				+ "to_date('"
				+ dataToInsert[11]
				+ "','YYYY-MM-DD'),"
				+ "to_date('"
				+ dataToInsert[12]
				+ "','YYYY-MM-DD'),"
				+ "'"
				+ dataToInsert[13]
				+ "',"
				+ "'"
				+ dataToInsert[14]
				+ "',"
				+ "'"
				+ dataToInsert[15] + "')";

		return insertText;
	}

	public HashMap<Integer, String[]> getLineItemsInsertsSet(int numberOfOrders) {
		HashMap<Integer, String[]> lineItemsInsertsSet = new HashMap<Integer, String[]>();
		for (int i = 0; i < numberOfOrders; i++) {
			lineItemsInsertsSet.put(i, getLineItemsInserts());
		}
		return lineItemsInsertsSet;
	}

	protected String[] getLineItemsInserts() {
		String[] inserts = new String[rand.nextInt(LINEITEMS_IN_ORDER + 1)];
		for (int i = 0; i < inserts.length; i++) {
			inserts[i] = getLineItemsInsert(i);
		}
		return inserts;
	}

	public String getParticularLineItem(int lineNumber) {
		return new String(lineItems[lineNumber]);
	}

	public String getParticularOrder(int lineNumber) {
		return new String(orders[lineNumber]);
	}
}
