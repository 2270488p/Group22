package Group22;

import java.sql.*;

public class Administrator
{

	Connection con = null;

	public Administrator(Connection con){
		this.con = con;
	}
	
	void dropTables()
	{

		try{
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
		}catch (SQLException e){
			System.out.println(e);
		}

	}
	
	void checkData() 
	{
		try{	
			String[] sql = new String[4];
			String[] tables = {"Driver", "Passenger", "Trip", "Request"};
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
				System.out.println(tables[i] + ": " + rs.getInt(1));
			}
		}catch (SQLException e){
			System.out.println(e);
		}
		
	}
	
	void createTables() 
	{
		try{		
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
					+ "(tripID INTEGER, start DATETIME, end DATETIME, fee INTEGER, driverID INTEGER NOT NULL, requestID INTEGER NOT NULL, passengerID INTEGER, rating INTEGER, "
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
		}catch (SQLException e){
			System.out.println(e);
		}
		
	}	
	

	void loadData(String path)
	{
		//load from path
	}
	
	void loadData(){ //this is a temporary method so we can try out inserting some random rows
        try{
            String[] sql = new String[10];
            sql[0] = "INSERT INTO Driver VALUES (14, 'anna', 'bla', 'toyota', 2018, 5)";
            sql[1] = "INSERT INTO Driver VALUES (15, 'lars', 'bla', 'bmw', 2018, 7)";
            sql[2] = "INSERT INTO Passenger VALUES (5, 'Trump')";
            sql[3] = "INSERT INTO Request VALUES (5, 0, NULL, NULL, 5, 5)";
            sql[4] = "INSERT INTO Passenger VALUES (6, 'Kanye')";
            sql[5] = "INSERT INTO Passenger VALUES (7, 'Spiderman')";
            sql[6] = "INSERT INTO Request VALUES (6, 0, 2018, 'TOYOTA prius', 4, 6)";
            sql[7] = "INSERT INTO Request VALUES (7, 0, 2018, 'large toyota', 3, 7)";
            sql[8] = "INSERT INTO Passenger VALUES (8, 'UberLover')";
            sql[9] = "INSERT INTO Request VALUES (8, 0, 2018, 'bmw', 5, 8)";
            Statement stmt = con.createStatement();
            for(int i = 0; i < sql.length; i++)
            {
                stmt.executeUpdate(sql[i]);
            }
        }catch(SQLException e){
            System.out.println(e);
        }
    }
	
	void getDatabaseMetaData() {
        try {

            DatabaseMetaData dbmd = con.getMetaData();
            String[] types = {"TABLE"};
            ResultSet rs = dbmd.getTables(null, null, "%", types);
            while (rs.next()) {
                System.out.println(rs.getString(3));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
}
