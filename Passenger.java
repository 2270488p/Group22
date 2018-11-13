import java.sql.*;
import java.util.Scanner;



public class Passenger 
{
	Connection con  = null;
	String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db22";
	String dbUsername = "Group22";
	String dbPassword = "CSCI3170";
	
	Scanner sc = new Scanner(System.in);
	
	
	
	public void requestRide() 
	{
		int pid, num_passengers, model_year;
		String model;
		System.out.println("Please enter your ID."); //!!!the inputs pid and num_passengers are mandatory!!!!!
		pid = sc.nextInt();
		System.out.println("Please enter the number of passengers.");
		num_passengers = sc.nextInt();
		System.out.println("Please enter the earliest model year.");
		num_passengers = sc.nextInt();
		System.out.println("Please enter the vehicle model.");
		
		//search for vehicles available (not in unfinished trips)
		//place request for matching trip	
	}
	
	
	
	public void checkTrips() {
		int pid;
		String start_date, end_date;
		//date format variable here: (YYYY-MM-DD);
		System.out.println("Please enter your ID."); //the inputs pid and num_passengers are mandatory!!
		pid = sc.nextInt();
		System.out.println("Please enter the start date (YYYY-MM-DD).");
		start_date = sc.nextLine();
		System.out.println("Please enter the end date (YYYY-MM-DD).");
		end_date = sc.nextLine();
		
		//split start and end
		//return all trips for passenger between start and end
	}
	
	
	
	public void rateTrip() 
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
			
			int passengerID, tripID, rating;				//asking for passengerID, tripID, rating
			System.out.println("Please enter your ID.");
			passengerID = sc.nextInt();
			System.out.println("Please enter the trip ID.");
			tripID = sc.nextInt();
			System.out.println("Please enter the rating.");
			rating = sc.nextInt();
			
			String sql1 = "UPDATE Trip "				//update rating
					+ "SET rating = ? "
					+ "WHERE tripID = ? AND passengerID = ?";
			PreparedStatement stmt1 = con.prepareStatement(sql1);
			stmt1.setInt(1, rating);
			stmt1.setInt(2, tripID);
			stmt1.setInt(3, passengerID);
			stmt1.executeUpdate();
			
			System.out.println("Trip ID, Driver name, Vehicle ID, Vehicle model, Start, End, Fee, Rating");				//print result
			String sql2 = "SELECT T.tripID, D.name, D.vehicleID, D.model, T.start, T.end, T.fee, T.rating "
					+ "FROM Trip T, Driver D "
					+ "WHERE T.passengerID = ? AND T.tripID = ?";
			PreparedStatement stmt2 = con.prepareStatement(sql2);
			stmt2.setInt(1, passengerID);
			stmt2.setInt(2, tripID);
			ResultSet rs = stmt2.executeQuery();
			rs.next();
			System.out.println(rs.getInt(1) + ", " + rs.getString(2) + ", " + rs.getString(3) + ", " + rs.getString(4) + ", " + rs.getString(5) + ", " + rs.getString(6) + ", " + rs.getInt(7) + ", " + rs.getInt(8));
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
