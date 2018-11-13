package Group22;

import java.sql.*;

public class Main 
{

	public static void main(String[] args) 
	{
		Connection con  = null;
		String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db22";
		String dbUsername = "Group22";
		String dbPassword = "CSCI3170";
		
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
			
			
			
			//CREATE TABLES
			/*String[] sql = new String[4];
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
			}*/
			
			
			
			//INSERT SOME RECORDS
			/*String[] sql = new String[5];
			sql[0] = "INSERT INTO Driver VALUES (14, 'anna', 'bla', 'model', 2018, 5)";
			sql[1] = "INSERT INTO Driver VALUES (15, 'lars', 'bla', 'model', 2018, 7)";
			sql[2] = "INSERT INTO Passenger VALUES (5, 'passenger')";
			sql[3] = "INSERT INTO Request VALUES (5, 0, 2018, 'model', 5, 5)";
			sql[4] = "INSERT INTO Trip VALUES (5, 'bla', 'blabla', 15, 14, 5, 5, 1)";
			Statement stmt = con.createStatement();
			for(int i = 0; i < 5; i++)
			{
				stmt.executeUpdate(sql[i]);
			}*/
			
			
			
			//CHECK DATA
			/*String[] sql = new String[4];
			sql[0] = "SELECT COUNT(*) FROM Driver";
			sql[1] = "SELECT COUNT(*) FROM Passenger";
			sql[2] = "SELECT COUNT(*) FROM Trip";
			sql[3] = "SELECT COUNT(*) FROM Request";
			Statement stmt = con.createStatement();
			System.out.println("Number of records in each table:");
			for(int i = 0; i < 4; i++)
			{
				ResultSet rs = stmt.executeQuery(sql[i]);
				rs.next();
				System.out.println("Table " + i + ": " + rs.getInt(1));
			}*/
			
			
			
			//DROP TABLES
			/*String[] sql = new String[4];
			sql[0] = "DROP TABLE IF EXISTS Trip";
			sql[1] = "DROP TABLE IF EXISTS Driver";
			sql[2] = "DROP TABLE IF EXISTS Request";
			sql[3] = "DROP TABLE IF EXISTS Passenger";
			Statement stmt = con.createStatement();
			for(int i = 0; i < 4; i++)
			{
				stmt.executeUpdate(sql[i]);
			}*/
			
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

}



