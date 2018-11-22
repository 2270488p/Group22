package Group22;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class Administrator
{

	Connection con = null;
	Scanner sc = new Scanner(System.in);
	static boolean tablesCreated = false;				//indicates whether tables are already created or not

	public Administrator(Connection con){
		this.con = con;
	}
	
	void dropTables()				//drops all tables in case they are created already
	{

		try{
			String[] sql = new String[4];
			sql[0] = "DROP TABLE IF EXISTS Trip";
			sql[1] = "DROP TABLE IF EXISTS Driver";
			sql[2] = "DROP TABLE IF EXISTS Request";
			sql[3] = "DROP TABLE IF EXISTS Passenger";
			Statement stmt = con.createStatement();
			for(int i = 0; i < 4; i++)
			{
				stmt.executeUpdate(sql[i]);
			}
			tablesCreated = false;
		}catch (SQLException e){
			System.out.println(e);
		}

	}
	
	void checkData()		//counts the number of data entries in each table and prints the result to the screen
	{
		try{	
			if(tablesCreated == false)				//check whether tables are created yet (cannot count number of data entries in each table if tables aren't created yet)
			{
				System.out.println("No tables created yet. You cannot check the data.");				//if tables arent't created print out error message
				return;
			}
			String[] sql = new String[4];
			String[] tables = {"Driver", "Passenger", "Trip", "Request"};
			sql[0] = "SELECT COUNT(*) FROM Driver";
			sql[1] = "SELECT COUNT(*) FROM Passenger";
			sql[2] = "SELECT COUNT(*) FROM Trip";
			sql[3] = "SELECT COUNT(*) FROM Request";
			Statement stmt = con.createStatement();
			System.out.println("Number of records in each table:");
			for(int i = 0; i < 4; i++)				//print result
			{
				ResultSet rs = stmt.executeQuery(sql[i]);
				rs.next();
				System.out.println(tables[i] + ": " + rs.getInt(1));
			}
		}catch (SQLException e){
			System.out.println(e);
		}
		
	}
	
	void createTables() 
	{
		try{		
			if(tablesCreated == true)				//checks whether tables are created yet (cannot create tables again if they are already created)
			{
				System.out.println("Tables are already created.");
				return;
			}
			System.out.println("Processing...");
			String[] sql = new String[4];
			sql[0] = "CREATE TABLE Driver"				//create Driver table
					+ "(driverID INTEGER, name CHAR(30), vehicleID CHAR(6), model CHAR(30), model_year INTEGER, seats INTEGER, "
					+ "PRIMARY KEY(driverID), UNIQUE(vehicleID))";
			sql[1] = "CREATE TABLE Passenger"				//create Passenger table
					+ "(passengerID INTEGER, name CHAR(30), PRIMARY KEY(passengerID))";
			sql[2] = "CREATE TABLE Request"				//create Request table
					+ "(requestID INTEGER, taken INTEGER, model_year INTEGER, model CHAR(30), passengers INTEGER, passengerID INTEGER NOT NULL, "
					+ "PRIMARY KEY(requestID), FOREIGN KEY (passengerID) REFERENCES Passenger(passengerID))";
			sql[3] = "CREATE TABLE Trip "				//create Trip table
					+ "(tripID INTEGER, start DATETIME, end DATETIME, fee INTEGER, driverID INTEGER NOT NULL, passengerID INTEGER, rating INTEGER, "
					+ "PRIMARY KEY(tripID), "
					+ "FOREIGN KEY(driverID) REFERENCES Driver(driverID), "
					+ "FOREIGN KEY(passengerID) REFERENCES Passenger(passengerID))";
			Statement stmt = con.createStatement();
			for(int i = 0; i < 4; i++)
			{
				stmt.executeUpdate(sql[i]);
			}
			System.out.print("Done! Tables are created!");
			tablesCreated = true;
		}catch (SQLException e){
			System.out.println(e);
		}
		
	}	

	
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	void loadData() {
		//checks whether tables are created yet (cannot create tables again if they are already created)
		if(tablesCreated == false) {
			//print error message
			System.out.println("Tables aren't created yet. You cannot insert data.");
			return;
		}
			
		//read in path to data files from administrator
		String datapath;				
		System.out.println("Please enter path to folder with datafiles.");
		datapath = sc.next();
		String drivers = File.separator + "drivers.csv";
		String passengers = File.separator + "passengers.csv";
		String trips = File.separator + "trips.csv";
		String vehicles = File.separator + "vehicles.csv";
		     
		BufferedReader br = null;
        	String line = "";
        	String cvsSplitBy = ",";
        
        	try {
        		//read in drivers.sv
        		br = new BufferedReader(new FileReader(datapath + drivers));				
		    	while ((line = br.readLine()) != null) {
				String[] result = line.split(cvsSplitBy);
				//insert tuples into Driver table
		    		String sql = "INSERT INTO Driver (driverID, name, vehicleID) VALUES (?, ?, ?)";
		    		PreparedStatement stmt = con.prepareStatement(sql);
		    		stmt.setInt(1, Integer.parseInt(result[0]));
		    		stmt.setString(2, result[1]);
		    		stmt.setString(3, result[2]);
		    		stmt.executeUpdate();
		    	}	    
		    
			if (br != null) {
				try {
					br.close();
	            		}
				catch (IOException e) {
					e.printStackTrace();
	            		}
	        	}
	        
	        	//read in passengers.csv
	       		br = new BufferedReader(new FileReader(datapath + passengers));
	        	//read passengers.csv line by line
	        	while ((line = br.readLine()) != null) {
		    		String[] result = line.split(cvsSplitBy);
		    		//insert tuples into Passenger table
		    		String sql = "INSERT INTO Passenger VALUES (?, ?)";
		    		PreparedStatement stmt = con.prepareStatement(sql);
		    		stmt.setInt(1, Integer.parseInt(result[0]));
		    		stmt.setString(2, result[1]);
		    		stmt.executeUpdate();
		    	}
		    
		    	if (br != null) {
	            		try {
	                		br.close();
	            		} 
	            		catch (IOException e) {
	                		e.printStackTrace();
	           	 	}
	        	}
		    
		    	//read in vehicles.csv
		    	br = new BufferedReader(new FileReader(datapath + vehicles));
		    	//read in file line by line
		    	while ((line = br.readLine()) != null) {
		    		String[] result = line.split(cvsSplitBy);
		    		//update model, model_year, seats in Driver table for tuple with matching vehicleID
		    		String sql = "UPDATE Driver " +
					"SET model = ?, model_year = ?, seats = ? " +
					"WHERE vehicleID = ?";
		    		PreparedStatement stmt = con.prepareStatement(sql);
		    		stmt.setString(1, result[1]);
		    		stmt.setInt(2,Integer.parseInt(result[2]));
		    		stmt.setInt(3, Integer.parseInt(result[3]));
		    		stmt.setString(4, result[0]);
		    		stmt.executeUpdate();
		    	}
		    
		    	if (br != null) {
	            		try {
	                		br.close();
	            		} 
	            		catch (IOException e) {
	                		e.printStackTrace();
	            		}
	        	}
		    
		    	//read in trips.csv
		    	br = new BufferedReader(new FileReader(datapath + trips));
		    	//read in file line by line//read in file line by line
		    	while ((line = br.readLine()) != null) {
		    		String[] result = line.split(cvsSplitBy);
		    		//insert tuples into Trip table
		    		String sql = "INSERT INTO Trip VALUES (?, ?, ?, ?, ?, ?, ?)";
		    		PreparedStatement stmt = con.prepareStatement(sql);
		    		stmt.setInt(1, Integer.parseInt(result[0]));
		    		stmt.setString(2, result[3]);
		    		stmt.setString(3, result[4]);
		    		stmt.setInt(4, Integer.parseInt(result[5]));
		    		stmt.setInt(5, Integer.parseInt(result[1]));
		    		stmt.setInt(6, Integer.parseInt(result[2]));
		    		stmt.setInt(7, Integer.parseInt(result[6]));
		    		stmt.executeUpdate();
		    	}	    
		    
	        	if (br != null) {
	            		try {
	                		br.close();
	            		} 
	            		catch (IOException e) {
	                		e.printStackTrace();
	            		}
	        	}
		}
			
		catch (SQLException e) {
			System.out.println(e);
		}
		
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		catch (IOException e) {
			e.printStackTrace();
		} 
	}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	void getDatabaseMetaData() {
        try {

            DatabaseMetaData dbmd = con.getMetaData();
            String[] types = {"TABLE"};
            ResultSet rs = dbmd.getTables(null, null, "%", types);
            while (rs.next()) {
                System.out.println(rs.getString(3));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
}
