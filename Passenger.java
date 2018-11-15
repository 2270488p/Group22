package Group22;

import java.sql.*;
import java.util.Scanner;

public class Passenger
{
    Connection con = null;
    Scanner sc = new Scanner(System.in);

    public Passenger(Connection con){
        this.con = con;
    }

    static int requestID = 0;				//request ID of last created request (negative integers)
	
	
	
	public void checkTrips() {
        try{
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
        } catch (SQLException e) {
            System.out.println(e);
        }

	}
	
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	public void requestRide() 
	{
		try
		{
			int passengerID, seats;				//variables for data entered by passenger
			String model_year, model;
			
			//read in passengers request
			System.out.println("Please enter your ID.");				//read in passengerID
			passengerID = sc.nextInt();
			System.out.println("Please enter the number of passengers.");				//read in number of seats
			seats = sc.nextInt();
			System.out.println("Please enter the earliest model year. (Press ENTER to skip)"); 				//read in model_year, in string format instead of integer format, no model_year possible  
			Scanner charScanner = new Scanner(System.in).useDelimiter("\\n");
			model_year = "";
			model_year += charScanner.nextLine();
			if(model_year.equals("")) model_year = null;
			System.out.println("Please enter the model.(Press ENTER to skip)");				//read in model, no model possible
			model = "";
			model += charScanner.nextLine();	
			if(model.equals("")) model = null;
			
			
			
			//SQL-Query to find out whether there are cars available matching the passengers request that are not in unfinished trips
			String sqlstart = "SELECT D.driverID FROM Driver D WHERE D.seats >= ?";			
			String model_yearsql = " AND D.model_year >= ?";
			String modelsql = " AND D.model LIKE ?";
			String sqlend = " AND D.driverID NOT IN (SELECT D2.driverID FROM Driver D2, Trip T WHERE T.driverID = D2.driverID AND T.end IS NULL)";

			PreparedStatement stmt;
			ResultSet rs;
			
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
			
			
			rs = stmt.executeQuery();
			
			if(!rs.isBeforeFirst())				//empty resultset, no car matching the the passengers request available at the moment
			{
				System.out.println("Sorry, no cars available matching your request");
			}
			else				//cars available matching the passengers request, create a new request and display number of available drivers (including those in unfinished trips)
			{
				//calculate the number of available drivers
				sqlstart = "SELECT COUNT(*) FROM Driver D WHERE seats >= ?";
			
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
				rs = stmt.executeQuery();
				rs.next();
				System.out.println("Your request is placed. " + rs.getInt(1) + " drivers are able to take your request.");
				
				
				//create new request
				requestID -= 1;				//decrement requestID
				String sql = "INSERT INTO Request VALUES (?, 0, ?, ?, ?, ?)";				//for untaken requests taken field has value 0
				stmt = con.prepareStatement(sql);
				stmt.setInt(1, requestID);
				stmt.setInt(4, seats);
				stmt.setInt(5, passengerID);
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
				stmt.executeUpdate();
			}

		}
		catch (SQLException e)
		{
			System.out.println(e);
		}	
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	public void rateTrip() 
	{
        try {
            int passengerID, tripID, rating;                //asking for passengerID, tripID, rating
            System.out.println("Please enter your ID.");
            passengerID = sc.nextInt();
            System.out.println("Please enter the trip ID.");
            tripID = sc.nextInt();
            System.out.println("Please enter the rating.");
            rating = sc.nextInt();

            String sql1 = "UPDATE Trip "                //update rating
                    + "SET rating = ? "
                    + "WHERE tripID = ? AND passengerID = ?";
            PreparedStatement stmt1 = con.prepareStatement(sql1);
            stmt1.setInt(1, rating);
            stmt1.setInt(2, tripID);
            stmt1.setInt(3, passengerID);
            stmt1.executeUpdate();

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
	
	
	
}
