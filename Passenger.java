package Group22;

import java.sql.*;
import java.util.Scanner;

public class Passenger
{
    Connection con = null;
    Scanner sc = new Scanner(System.in);

    public Passenger(Connection con)
    {
        this.con = con;
    }

    static int requestID = 0;				//request ID of last created request (negative integers)
	
	
    
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	public void checkTripRecords()				//prints all finished Trips within a given period of time and a given PassengerID to the screen
	{
        try
        {		
        	if(Administrator.tablesCreated == false)				//check whether tables are created yet (cannot execute queries if tables aren't created yet)
			{
				System.out.println("Sorry tables aren't created yet. No action possible.");				//if tables aren't created yet print error message
				return;
			}
        	
        	//read in user input
			int passengerID;
			String start, end;
			System.out.println("Please enter your ID.");				//read in passengerID
			passengerID = sc.nextInt();
			if(checkPassengerID(passengerID) == false)				//check whether given passengerID is valid
			{
				System.out.println("This is not a valid PassengerID");				//if passengerID not valid print error message
				return;
			}
			System.out.println("Please enter the start date (YYYY-MM-DD).");				//read in start date
			start = sc.next();
			System.out.println("Please enter the end date (YYYY-MM-DD).");				//read in end date
			end = sc.next();
		
			//SQL-Query
			String sql = "SELECT T.tripID, D.name, D.vehicleID, D.model, T.start, T.end, T.fee, T.rating "
					+ "FROM Trip T, Driver D "
					+ "WHERE T.driverID = D.driverID AND passengerID = ? AND start >= ? AND end <= ? "
					+ "ORDER BY T.start DESC";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, passengerID);
			stmt.setString(2, start);
			stmt.setString(3, end + " 23:59:59");
			ResultSet rs = stmt.executeQuery();				//execute SQL-query
			
			
			//print out result
			if(!rs.isBeforeFirst())				//if no matching finished trips in the requested time period 
			{
				System.out.println("No finished trips during your requested time period.");				//print out error message
			}
			else				//passenger has finished trips in the requested time period, print them to the screen
			{
				System.out.println("Trip ID, Driver Name, Vehicle ID, Vehicle Model, Start, End, Fee, Rating");
				while(rs.next())
				{
					System.out.println(rs.getInt(1) + ", " + rs.getString(2) + ", " + rs.getString(3) + ", " + rs.getString(4) + ", " + rs.getString(5) + ", " + rs.getString(6) + ", " + rs.getInt(7) + ", " + rs.getInt(8));
				}
			}
        } 
        catch (SQLException e) 
        {
            System.out.println(e);
        }

	}
	
	
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	public void requestRide() 
	{
		try
		{
			if(Administrator.tablesCreated == false)				//check whether tables are created yet (cannot execute query if tables aren't created yet)
			{
				System.out.println("Sorry tables aren't created yet. No action possible.");				//if tables aren't created yet print error message
				return;
			}
			
			int passengerID, seats;				//variables for data entered by passenger
			String model_year, model;
			
			//read in passengers request
			System.out.println("Please enter your ID.");				//read in passengerID
			passengerID = sc.nextInt();
			if(checkPassengerID(passengerID) == false)				//check if given passengerID is valid
			{
				System.out.println("This is not a valid PassengerID");				//if passengerID not valid print out error message
				return;
			}
			String openRequest = "SELECT COUNT(*) FROM Request WHERE taken = 0 AND passengerID = ?";				//for untaken requests taken field has value 0
			PreparedStatement openRequestStmt= con.prepareStatement(openRequest);				//check whether there is another open request by this passenger
			openRequestStmt.setInt(1, passengerID);
			ResultSet openRequestRs = openRequestStmt.executeQuery();
			openRequestRs.next();
			int numberOpenRequest = openRequestRs.getInt(1);
			if(numberOpenRequest > 0)				//passenger cannot take place another request if there is still an open one by him
			{
				System.out.println("There is still an open Request by you. You cannot place another one.");				//print error message
				return;
			}
			System.out.println("Please enter the number of passengers.");				//read in number of seats
			seats = sc.nextInt();
			if(seats < 1 || seats > 8)				//only 1-8 passengers possible for a request
			{
				System.out.println("Sorry, only cars with 1 to 8 seats available");				//print out error message if requested number of seats is not in the range of 1-8
				return;
			}
			System.out.println("Please enter the earliest model year. (Press ENTER to skip)"); 				//read in model_year, in string format instead of integer format, no model_year possible  
			Scanner charScanner = new Scanner(System.in).useDelimiter("\\n");
			model_year = "";
			model_year += charScanner.nextLine();
			if(model_year.equals("")) model_year = null;
			System.out.println("Please enter the model.(Press ENTER to skip)");				//read in model, no model possible
			model = "";
			model += charScanner.nextLine();
			if(model.length() > 30)				//model criterion cannot be longer than 30 characters 
			{
				System.out.println("Your model criterion is too long. Maximum 30 characters allowed.");				//print out error message if model criterion too long
				return;
			}
			if(model.equals("")) model = null;
			
			
			
			//SQL-Query to find out whether there are cars available matching the passengers request that are not in unfinished trips
			String sqlstart = "SELECT D.driverID FROM Driver D WHERE D.seats >= ?";			
			String model_yearsql = " AND D.model_year >= ?";
			String modelsql = " AND D.model LIKE ?";
			String sqlend = " AND D.driverID NOT IN (SELECT D2.driverID FROM Driver D2, Trip T WHERE T.driverID = D2.driverID AND T.end IS NULL)";

			PreparedStatement stmt;
			ResultSet rs;
			
			
			//differentiate 4 cases
			if(model_year == null)
			{
				if(model == null)				//no model_year preference, no model preference
				{
					stmt = con.prepareStatement(sqlstart + sqlend);
					stmt.setInt(1, seats);
				}
				else				//no model_year preference but given model preference
				{
					stmt = con.prepareStatement(sqlstart + modelsql + sqlend); 
					stmt.setInt(1, seats);
					String modelstring = "%" + model + "%";
					stmt.setString(2, modelstring);
				}
			}
			else
			{
				if(model == null)				//given model_year preference but no model preference
				{
					stmt = con.prepareStatement(sqlstart + model_yearsql + sqlend); 
					stmt.setInt(1, seats);
					int model_yearint = Integer.parseInt(model_year);
					stmt.setInt(2, model_yearint);
				}
				else				//given model_year preference and given model preference
				{
					stmt = con.prepareStatement(sqlstart + model_yearsql + modelsql + sqlend);
					stmt.setInt(1, seats);
					int model_yearint = Integer.parseInt(model_year);
					stmt.setInt(2, model_yearint);
					String modelstring = "%" + model + "%";
					stmt.setString(3, modelstring);
				}
			}
			
			
			rs = stmt.executeQuery();				//execute SQL-query
			
			if(!rs.isBeforeFirst())				//empty resultset, no car matching the the passengers request available at the moment
			{
				System.out.println("Sorry, no cars available matching your request");				//print out error message
			}
			else				//cars available matching the passengers request, create a new request and display number of available drivers (including those in unfinished trips)
			{
				//calculate the number of available drivers
				sqlstart = "SELECT COUNT(*) FROM Driver D WHERE seats >= ?";
			
				//differentiate between 4 cases
				if(model_year == null)
				{
					if(model == null)				//no model_year preference, no model preference
					{
						stmt = con.prepareStatement(sqlstart);
						stmt.setInt(1, seats);
					}
					else				//no model_year preference but given model preference
					{
						stmt = con.prepareStatement(sqlstart + modelsql); 
						stmt.setInt(1, seats);
						String modelstring = "%" + model + "%";
						stmt.setString(2, modelstring);
					}
				}
				else
				{
					if(model == null)				//given model_year preference but no model preference
					{
						stmt = con.prepareStatement(sqlstart + model_yearsql); 
						stmt.setInt(1, seats);
						int model_yearint = Integer.parseInt(model_year);
						stmt.setInt(2, model_yearint);
					}
					else				//given model_year preference and given model preference
					{
						stmt = con.prepareStatement(sqlstart + model_yearsql + modelsql);
						stmt.setInt(1, seats);
						int model_yearint = Integer.parseInt(model_year);
						stmt.setInt(2, model_yearint);
						String modelstring = "%" + model + "%";
						stmt.setString(3, modelstring);
					}
				}
				rs = stmt.executeQuery();				//execute SQL-query
				rs.next();
				System.out.println("Your request is placed. " + rs.getInt(1) + " drivers are able to take your request.");				//print out number of drivers able to take the trip (including those still in unfinished trips)
				
				
				//create new request
				requestID -= 1;				//decrement requestID to create new unique requestID for new Request
				String sql = "INSERT INTO Request VALUES (?, 0, ?, ?, ?, ?)";				//for untaken requests taken field has value 0
				stmt = con.prepareStatement(sql);
				stmt.setInt(1, requestID);
				stmt.setInt(4, seats);
				stmt.setInt(5, passengerID);
				//differentiate between 4 cases
				if(model_year == null)				//no model_year preference
				{
					stmt.setNull(2, Types.INTEGER);				//set model_year field NULL
				}
				else				//with model_year preference 
				{
					stmt.setInt(2, Integer.parseInt(model_year));
				}
				if(model == null)				//no model preference
				{
					stmt.setString(3, null);				//set model field NULL
				}
				else				//with model preference
				{
					stmt.setString(3, model);
				}
				stmt.executeUpdate();				//execute SQL-query to insert new Request into Request table
			}

		}
		catch (SQLException e)
		{
			System.out.println(e);
		}	
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	public void rateTrip()				//a Passenger can rate his/her finished Trips
	{
        try {
        	if(Administrator.tablesCreated == false)				//check whether tables are created yet (cannot execute queries if tables aren't created yet)
			{
				System.out.println("Sorry tables aren't created yet. No action possible.");				//print out error message if tables aren't created yet
				return;
			}
        	
            int passengerID, tripID, rating;                //asking for passengerID, tripID, rating
            System.out.println("Please enter your ID.");				//read in passengerID
            passengerID = sc.nextInt();
            if(checkPassengerID(passengerID) == false)				//check whether given passengerID is valid
			{
				System.out.println("This is not a valid PassengerID");				//print out error message if not valid
				return;
			}
            System.out.println("Please enter the trip ID.");				//read in tripID
            tripID = sc.nextInt();
            System.out.println("Please enter the rating.");				//read in rating
            rating = sc.nextInt();
            if(rating < 1 || rating > 5 )				//value of rating must be in range of 1-5
			{
				System.out.println("Only ratings from 1-5 possible");				//print error if rating value is not within the allowed range
				return;
			}
            
            //check whether there exists a finished Trip with the given passengerID and tripID
            String FTsql = "SELECT COUNT(*) FROM Trip WHERE passengerID = ? AND tripID = ? AND end IS NOT NULL";				//FT finished trip
			PreparedStatement FTstmt = con.prepareStatement(FTsql);
			FTstmt.setInt(1, passengerID);
			FTstmt.setInt(2, tripID);
			ResultSet FTrs = FTstmt.executeQuery();
			FTrs.next();
			int FT = FTrs.getInt(1);
			if(FT == 0) 
			{
				System.out.println("There is no finished trip matching your request.");				//no matching trip found for rating, print error message
				return;
			}
            
			//SQL-query to set the ratin
            String sql1 = "UPDATE Trip "                //update rating
                    + "SET rating = ? "
                    + "WHERE tripID = ? AND passengerID = ?";
            PreparedStatement stmt1 = con.prepareStatement(sql1);
            stmt1.setInt(1, rating);
            stmt1.setInt(2, tripID);
            stmt1.setInt(3, passengerID);
            stmt1.executeUpdate();				//execute the query

            System.out.println("Trip ID, Driver name, Vehicle ID, Vehicle model, Start, End, Fee, Rating");                //print result
            String sql2 = "SELECT T.tripID, D.name, D.vehicleID, D.model, T.start, T.end, T.fee, T.rating "
                    + "FROM Trip T, Driver D "
                    + "WHERE T.passengerID = ? AND T.tripID = ?";
            PreparedStatement stmt2 = con.prepareStatement(sql2);
            stmt2.setInt(1, passengerID);
            stmt2.setInt(2, tripID);
            ResultSet rs = stmt2.executeQuery();
            rs.next();
            System.out.println(rs.getInt(1) + ", " + rs.getString(2) + ", " + rs.getString(3) + ", " + rs.getString(4) + ", " + rs.getString(5) + ", " + rs.getString(6) + ", " + rs.getInt(7) + ", " + rs.getInt(8));

        } catch (SQLException e) {
            System.out.println(e);
        }
    }
	
	
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public boolean checkPassengerID(int pid)				//checks whether a given integer is a valid passengerID or not
	{ 
        try{
        	//SQL-query
            String sql = "SELECT passengerID " +
                    "FROM Passenger " +
                    "WHERE passengerID = ? ";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, pid);
            ResultSet rs = stmt.executeQuery();
            if(!rs.next()) return false;				//if ResultSet rs is empty the given integer is not a valid passenger ID, return false
            else return true;				//given integer is a valid passengerID, return true
        }
        catch(SQLException e)
        {
            return false;
        }
    }

	
	
}
