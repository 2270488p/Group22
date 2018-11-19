package Group22;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.sql.*;
public class Driver {
	
	Connection con = null;
    Scanner sc = new Scanner(System.in);

    public Driver(Connection con){
        this.con = con;
    }
	
	public void takeRequest() {

        try{
            int rid;
            int did = askForDriverID();

            if(!checkDriverID(did)){
                System.out.println("Invalid driver ID.");
                return;
            }

            if(isInUnfinishedTrip(did)){
                System.out.println("You have to finish your current trip before taking a new request.");
                return;
            }

            String[] sql = new String[6];
            sql[0] = "SELECT D.model FROM Driver D WHERE D.driverID = " + did;
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql[0]);
            rs.next();
            String driverModel = rs.getString(1);

            sql[1] = "SELECT R.requestID, P.name, R.passengers " +
                    "FROM Passenger P, Request R, Driver D " +
                    "WHERE P.passengerID = R.passengerID AND R.taken = 0 AND D.seats >= R.passengers AND D.driverID = ? AND " +
                    "(R.model_year IS NULL OR D.model_year >= R.model_year) AND (R.model IS NULL OR R.model LIKE '%" + driverModel + "%')";
            List<Integer> possibleReqs = new LinkedList<>();
            PreparedStatement tempstmt = con.prepareStatement(sql[1]);
            tempstmt.setInt(1, did);
            ResultSet resset = tempstmt.executeQuery();
            System.out.println("Request ID, Passenger name, Passengers");
            while(resset.next()){
                System.out.printf("%d, %s, %d\n", resset.getInt(1), resset.getString(2), resset.getInt(3));
                possibleReqs.add(resset.getInt(1));
            }

            do{
                System.out.println("Please enter a request ID.");
                while (!sc.hasNextInt()) {
                    System.out.println("That is not a valid request id. The request ID has to be a positive integer.");
                    sc.next();
                }
                rid = sc.nextInt();
                if(rid < 1){
                    System.out.println("That is not a valid request id. The request ID has to be a positive integer.");
                }
            } while (rid < 1);
            boolean flag = false;
            for(int i = 0; i < possibleReqs.size(); i++){
                if(possibleReqs.get(i) == rid){
                    flag = true;
                    break;
                }
            }

            if(!flag){
                System.out.println("You picked an invalid request. Please pick a request from the list.");
                return;
            }

            sql[2] = "SELECT P.name, P.passengerID " +
                    "FROM Passenger P, Request R " +
                    "WHERE P.passengerID = R.passengerID AND R.requestID = ?";
            PreparedStatement stmt2 = con.prepareStatement(sql[2]);
            stmt2.setInt(1, rid);
            rs = stmt2.executeQuery();
            rs.next();
            String passengerName = rs.getString(1);
            int passengerID = rs.getInt(2);
            sql[3] = "UPDATE Request " +
                    "SET taken = 1 " +
                    "WHERE taken = 0 AND requestID = ?";

            PreparedStatement stmt3 = con.prepareStatement(sql[3]);
            stmt3.setInt(1, rid);
            stmt3.executeUpdate();

            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
            sql[4] = "INSERT INTO Trip (tripID, start, driverID, passengerID) " +
                    "VALUES (?, ?, ?, ?)";
            PreparedStatement stmt4 = con.prepareStatement(sql[4]);
            stmt4.setInt(1, rid);
            stmt4.setString(2, timeStamp);
            stmt4.setInt(3, did);
            stmt4.setInt(4, passengerID);
            stmt4.executeUpdate();
            int tripID = rid;
            System.out.println("Trip ID, Passenger name, Start");
            System.out.printf("%d, %s, %s\n", tripID, passengerName, timeStamp);
        }catch (SQLException e){
            System.out.println(e);
        }

	}
	
	public void finishTrip() {
        try{
            int did = askForDriverID();

            if(!checkDriverID(did)){
                System.out.println("Invalid driver ID.");
                return;
            }

            if(!isInUnfinishedTrip(did)){
                System.out.println("You have no unfinished trips. Take a request first.");
                return;
            }
            String sql[] = new String[5];
            sql[0] = "SELECT T.tripID, T.passengerID, T.start " +
                    "FROM Trip T " +
                    "WHERE T.driverID = ? AND T.end IS NULL";
            PreparedStatement stmt = con.prepareStatement(sql[0]);
            stmt.setInt(1, did);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            int tripID = rs.getInt(1);
            int passengerID = rs.getInt(2);
            String start = rs.getString(3);
            System.out.println("Trip ID, Passenger ID, Start");
            System.out.printf("%d, %d, %s\n", tripID, passengerID, start);

            String temp;
            Scanner strsc = new Scanner(System.in);
            System.out.println("Do you want to finish this trip? [y/n]");
            temp = strsc.nextLine();
            temp = temp.toLowerCase();

            if(temp.equals("y")) {
                sql[2] = "SELECT P.name " +
                        "FROM Passenger P, Trip T " +
                        "WHERE T.driverID = ? AND T.end IS NULL AND P.passengerID = T.passengerID";
                PreparedStatement stmt3 = con.prepareStatement(sql[2]);
                stmt3.setInt(1, did);
                rs = stmt3.executeQuery();
                rs.next();
                String passengerName = rs.getString(1);

                String end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
                sql[1] = "UPDATE Trip T " +
                        "SET end = ?, fee = (TIME_TO_SEC(TIMEDIFF(?,?))/60) " +
                        "WHERE T.driverID = ? AND T.end IS NULL";
                PreparedStatement stmt2 = con.prepareStatement(sql[1]);
                stmt2.setString(1, end);
                stmt2.setString(2, end);
                stmt2.setString(3, start);
                stmt2.setInt(4, did);
                stmt2.executeUpdate();
                sql[3] = "SELECT T.fee " +
                        "FROM Trip T " +
                        "WHERE T.driverID = ? AND T.start = ? AND T.end = ?";
                PreparedStatement stmt4 = con.prepareStatement(sql[3]);
                stmt4.setInt(1, did);
                stmt4.setString(2, start);
                stmt4.setString(3, end);
                rs = stmt4.executeQuery();
                rs.next();
                int fee = rs.getInt(1);

                System.out.println("Trip ID, Passenger name, Start, End, Fee");
                System.out.printf("%d, %s, %s, %s, %d\n", tripID, passengerName, start, end, fee);


            }else if (temp.equals("n")){
                return;
            }else{
                System.out.println("Invalid answer. Type in either y for yes or n for no.");
                return;
            }

        }catch(SQLException e){
            System.out.println(e);
        }
		
	}

	public void checkDriverRating() {
        try{
            int did = askForDriverID();

            if(!checkDriverID(did)){
                System.out.println("Invalid driver ID.");
                return;
            }

            int count = 0;
            int sum = 0;
            String[] sql = new String [3];
            sql[0] = "SELECT T.rating " +
                    "FROM Trip T " +
                    "WHERE T.driverID = ? AND T.rating != 0 " +
                    "ORDER BY end DESC ";
            PreparedStatement stmt = con.prepareStatement(sql[0]);
            stmt.setInt(1, did);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                sum += rs.getInt(1);
                System.out.println(rs.getInt(1));
                count++;
            }


            if(count<5) {
                System.out.printf("You don't have an average rating yet. You need %s more rating(s).", 5-count);
            }else {
                double avg = Math.round(sum * 100.0) / 100.0; //display driver rating rounded to 2 decimal places
                System.out.println("Average is " + avg);
            }

        }catch(SQLException e){
            System.out.println(e);
        }

	}

	public boolean isInUnfinishedTrip(int did){
        try{
            String query = "SELECT COUNT(*) " +
                    "FROM Trip T " +
                    "WHERE T.driverID = ? AND T.end IS NULL";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, did);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            int result = rs.getInt(1);

            if(result > 0){
                return true;
            }
            return false;
        }catch(SQLException e){
            System.out.println(e);
        }

        return false;
    }

    public int askForDriverID(){
        int did;
        do{
            System.out.println("Please enter your ID.");
            while (!sc.hasNextInt()) {
                System.out.println("That is not a valid driver id. Your driver ID has to be a positive integer.");
                sc.next();
            }
            did = sc.nextInt();
            if(did < 1){
                System.out.println("That is not a valid driver id. Your driver ID has to be a positive integer.");
            }
        } while (did < 1);

        return did;
    }

    public boolean checkDriverID(int did){
        try{
            String sql = "SELECT DriverID " +
                    "FROM Driver " +
                    "WHERE DriverID = ? ";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, did);
            ResultSet resset = stmt.executeQuery();
            if(!resset.next()){
                return false;
            }return true;
        }catch(SQLException e){
            return false;
        }
    }
	
	

}
