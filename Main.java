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
		Scanner sc = new Scanner(System.in);
		int number;

        do {
            System.out.println("Welcome! Who are you?");
            System.out.println("1. An administrator");
            System.out.println("2. A passenger");
            System.out.println("3. A driver");
            System.out.println("4. None of the above");
            while (!sc.hasNextInt()) {
                System.out.println("You must enter a number from 1 to 4.");
                sc.next();
            }
            number = sc.nextInt();
        } while (number < 1 || number > 4);
		
		if(number == 1){
			Administrator admin = new Administrator(con);
			System.out.println(
			
		}else if(number == 2){
			Passenger passenger = new Passenger(con);
			
		}else if(number == 3){
			Driver driver = new Driver(con);
			
		}else if(number == 4){
			
		}
		
		
		// switch(number){
			// case 1: 
			// case 2:
			// case 3:
			// case 4:
		// }


		//remaining code here
		
	}

}



