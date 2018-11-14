package Group22;

import java.sql.*;
import java.util.Scanner;

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
		
		checkStatus(con);

		
	}
	
	public static int welcomeMessage(){
        int number;
        Scanner sc = new Scanner(System.in);
        do {
            System.out.println("Welcome! Who are you?");
            System.out.println("1. An administrator");
            System.out.println("2. A passenger");
            System.out.println("3. A driver");
            System.out.println("4. None of the above");
            while (!sc.hasNextInt()) {
                System.out.println("That is not a valid number.");
                sc.next();
            }
            number = sc.nextInt();
            if(number < 1 || number > 4){
                System.out.println("Please enter a number between 1 and 4.");
            }
        } while (number < 1 || number > 4);

        return number;

    }

    public static void checkStatus(Connection con){
        int number = welcomeMessage();
        Scanner sc = new Scanner(System.in);

        if(number == 1){
            Group22.Administrator admin = new Administrator(con);
            do {
                System.out.println("Administrator, what would you like to do?");
                System.out.println("1. Create tables");
                System.out.println("2. Delete tables");
                System.out.println("3. Load data");
                System.out.println("4. Check data");
                System.out.println("5. Go back");
                while (!sc.hasNextInt()) {
                    System.out.println("That is not a valid number.");
                    sc.next();
                }
                number = sc.nextInt();
                if(number < 1 || number > 5){
                    System.out.println("Please enter a number between 1 and 5.");
                }
            } while (number < 1 || number > 5);

            if(number == 1){
                //admin.createTables();
                System.out.println("tables created");
            }else if(number == 2){
                System.out.println("tables deleted");
            }else if(number == 3){
                System.out.println("data loaded");
            }else if(number == 4){
                System.out.println("data checked");
            }else{
                checkStatus(con);
            }

        }else if(number == 2){
            Group22.Passenger passenger = new Passenger(con);
            do {
                System.out.println("Passenger, what would you like to do?");
                System.out.println("1. Request a ride");
                System.out.println("2. Check trip records");
                System.out.println("3. Rate a trip");
                System.out.println("4. Go back");
                while (!sc.hasNextInt()) {
                    System.out.println("That is not a valid number.");
                    sc.next();
                }
                number = sc.nextInt();
                if(number < 1 || number > 4){
                    System.out.println("Please enter a number between 1 and 4.");
                }
            } while (number < 1 || number > 4);

            if(number == 1){
                System.out.println("requesting ride");
            }else if(number == 2){
                System.out.println("checking trip records");
            }else if(number == 3){
                System.out.println("rating trip");
            }else{
                checkStatus(con);
            }

        }else if(number == 3){
            Group22.Driver driver = new Driver(con);
            do {
                System.out.println("Driver, what would you like to do?");
                System.out.println("1. Take request");
                System.out.println("2. Finish a trip");
                System.out.println("3. Check driver rating");
                System.out.println("4. Go back");
                while (!sc.hasNextInt()) {
                    System.out.println("That is not a valid number.");
                    sc.next();
                }
                number = sc.nextInt();
                if(number < 1 || number > 4){
                    System.out.println("Please enter a number between 1 and 4.");
                }
            } while (number < 1 || number > 4);

            if(number == 1){
                System.out.println("taking request");
            }else if(number == 2){
                System.out.println("finishing trip");
            }else if(number == 3){
                System.out.println("checking rating");
            }else{
                checkStatus(con);
            }

        }else if(number == 4){
            System.out.println("You must be either an administrator, passenger, ir driver to access this database. Goodbye.");
        }
    }

	
	
	

}



