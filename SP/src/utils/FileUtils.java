package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileUtils {

	private ArrayList<String> lineItemsList;
	private ArrayList<String> ordersList;
	
	public FileUtils()
	{
		lineItemsList = readLineItems();
		ordersList = readOrders();
	}
	
	private ArrayList<String> readLineItems()
	{
		ArrayList<String> lineItems = new ArrayList<String>();
		Scanner scanner = null;
		
		try {
			scanner = new Scanner(new File("InsertDataSets/lineitem.tbl.u0"));
			
	        while (scanner.hasNextLine()) {
	           lineItems.add(scanner.nextLine());
	        }
	        
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
		
		return lineItems;
	}
	
	private ArrayList<String> readOrders()
	{
		ArrayList<String> orders = new ArrayList<String>();
		Scanner scanner = null;
		
		try {
			scanner = new Scanner(new File("InsertDataSets/orders.tbl.u0"));
			
	        while (scanner.hasNextLine()) {
	           orders.add(scanner.nextLine());
	        }
	        
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
		
		return orders;
	}
	
	public String getParticularLineItem(int lineNumber)
	{
		return lineItemsList.get(lineNumber);
	}
	
	public String getParticularOrder(int lineNumber)
	{
		return ordersList.get(lineNumber);
	}
	
}
