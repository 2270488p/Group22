# Ridesharing System - Group42
Description of the Ridesharing System Project submitted on 2018 November, 23rd containing the java source code used for the implementation of the system

## Overview
This file along with the source code is submitted in ZIP format by PETROVA, Mihaela Plamenova (SID: 1155123138)
  
### Prerequisites
Connection to the database is needed:
1) VPN and user account information provided for using the database
2) Installation of SQL, Java Editor, Putty and SSH

### Implementation
Administartor.java file:
a) createTable() 
4 Tables are created - Driver, Passenger, Request and Trip

b) loadData()
Do not load data if tables are not created
Read from the 4 CSV files provided
Go through each file line by line and separate them with ','
Instert tuples into each table
Update Driver table if preferences for the vehicle are set by the passenger

