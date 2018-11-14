package Group22;

import java.util.Scanner;
public class Driver {
	
	Connection con = null;
    Scanner sc = new Scanner(System.in);

    public Driver(Connection con){
        this.con = con;
    }
	
	public void takeRequest() {
		
		int did, rid;
		System.out.println("Please enter your ID.");
		did = sc.nextInt();
		//first search if driver is in a trip already. if he is, he cannot take another request so return error

		//here search and print out all open requests for which the driver is qualified based on his vehicle seats, model, and model year
		//print the list by ID, passenger name, and num of passengers
		
		if(/*result is non-empty (driver is qualified for at least 1 trip*/) {
			System.out.println("Please enter the request ID.");
			rid = sc.nextInt();
			//mark the request as taken, create a new trip, and display the trip by its ID, passenger name, and start time
		}else {
			System.out.println("No trips sorry bro.");
		}
	}
	
	public void finishTrip() {
		int did, tid;
		System.out.println("Please enter your ID.");
		did = sc.nextInt();
		
		if(/*driver is in an unfinished trip*/) {
			//display the trip by its ID, the passenger's ID, and the start time.
			System.out.println("Do you want to finish this trip? [y/n]");
			String temp = sc.nextLine();
			if(temp.equals("y")) {
				//record the end time in the trip's column and calculate the fee for the trip
				//the fee is the duration of the trip in minutes, rounded down to nearest int
				//display trip by ID, the passenger name, start, end and fee
			}else {
				continue; //do nothing
			}

		}else {
			System.out.println("Sorry you are not in a trip so you cannot finish it.");
		}
		
		
	}
	
	public void checkDriverRating() {
		int did;
		System.out.println("Please enter your ID.");
		did = sc.nextInt();
		int count = 0;
		//query for number of ratings; assign that to count
		if(count<5) {
			//driver doesn't have ratings yet
			System.out.printf("You don't have an average rating yet. You need %s more rating(s).", 5-count);
		}else {
			//find last 5 entries of ratings (go by finish time)
			//find the average of the five
			//display driver rating rounded to 2 decimal places
		}
	}
	
	

}
