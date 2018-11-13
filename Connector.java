import java.sql.*;

public class Connector {
	
	Connection con  = null;
	String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db22";
	String dbUsername = "Group22";
	String dbPassword = "CSCI3170";
	
	
	public void connect() {
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
			//String sql = "CREATE TABLE S1(sid integer primary key, name varchar(30) not null, year integer, age integer)";
			//Statement stmt = con.createStatement();
			//stmt.executeUpdate(sql); 
			String bla = "INSERT INTO S1 VALUES (14, 'anna', 1, 12)";
			Statement b = con.createStatement();
			b.executeUpdate(bla);
			String sql = "SELECT name, age FROM S1";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if(!rs.isBeforeFirst()) 
			{
				System.out.println("No records found.");
			}
			while(rs.next())
			{
				System.out.print(rs.getString(1));
				System.out.print(rs.getInt("age"));
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
}
	
