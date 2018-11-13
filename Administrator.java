import java.sql.*;

public class Administrator 
{
	Connection con  = null;
	String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db22";
	String dbUsername = "Group22";
	String dbPassword = "CSCI3170";
	//mishi
	
	void dropTables()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");				//drop tables
			con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
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
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("[Error]: Java MySQL DB Driver not found!!");
			System.exit(0);
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}
	
	void checkData() 
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");				//count number of entries in each table
			con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
			String[] sql = new String[4];
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
				System.out.println("Table " + i + ": " + rs.getInt(1));
			}
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("[Error]: Java MySQL DB Driver not found!!");
			System.exit(0);
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}
	
	void createTables() 
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");				//create tables
			con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
			String[] sql = new String[4];
			sql[0] = "CREATE TABLE Driver"
					+ "(driverID INTEGER, name CHAR(30), vehicleID CHAR(6), model CHAR(30), model_year INTEGER, seats INTEGER, "
					+ "PRIMARY KEY(driverID))";
			sql[1] = "CREATE TABLE Passenger"
					+ "(passengerID INTEGER, name CHAR(30), PRIMARY KEY(passengerID))";
			sql[2] = "CREATE TABLE Request"
					+ "(requestID INTEGER, taken INTEGER, model_year INTEGER, model CHAR(30), passengers INTEGER, passengerID INTEGER NOT NULL, "
					+ "PRIMARY KEY(requestID), FOREIGN KEY (passengerID) REFERENCES Passenger(passengerID))";
			sql[3] = "CREATE TABLE Trip "
					+ "(tripID INTEGER, start CHAR(19), end CHAR(19), fee INTEGER, driverID INTEGER NOT NULL, requestID INTEGER NOT NULL, passengerID INTEGER, rating INTEGER, "
					+ "PRIMARY KEY(tripID), "
					+ "UNIQUE(requestID), "
					+ "FOREIGN KEY(driverID) REFERENCES Driver(driverID), "
					+ "FOREIGN KEY(requestID) REFERENCES Request(requestID), "
					+ "FOREIGN KEY(passengerID) REFERENCES Passenger(passengerID))";
			Statement stmt = con.createStatement();
			for(int i = 0; i < 4; i++)
			{
				stmt.executeUpdate(sql[i]);
			}
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("[Error]: Java MySQL DB Driver not found!!");
			System.exit(0);
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}	
	

	void loadData(String path)
	{
		//load from path
	}
	
}
