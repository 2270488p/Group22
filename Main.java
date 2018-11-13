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

		//remaining code here
		
	}

}



