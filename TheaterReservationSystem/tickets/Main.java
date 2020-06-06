//Author: Jonathan Harvey
package tickets;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
	static HashMap<String, User> userMap; //stores usernames and  corresponding User objects
	public static void main(String[] args) throws IOException {
		
		Auditorium a0 = new Auditorium("A1.txt");
		Auditorium a1 = new Auditorium("A2.txt");
		Auditorium a2 = new Auditorium("A3.txt");
		Auditorium audList[] = {a0, a1, a2};
		Scanner input = new Scanner(System.in);
		userMap = readUserData("userdb.dat");
		loadUserOrders();
		while (true) {
			String username;
			String password;
			System.out.print("username: ");
			username = input.next();
			for (int i=0; i<3; i++) {
				System.out.print("password: ");
				password = input.next();
				if (userMap.containsKey(username)) {
					if (password.equals(userMap.get(username).getPassword()))
					{
						if (username.equals("admin")) {
							adminMenu(audList, input);
							break;
						} else {
							userMenu(audList, input, username);
							break;
						}
					}
				}
				System.out.println("invalid username or password\n");
			}
			System.out.println();
			continue;
			
			
		}
		//Auditorium a0 = new Auditorium("A1.txt");
		
		//userMenu(a0, input);
		//a0.writeToFile("A1.txt");
		
		//a0.displayReport();
	}
	
	//controls flow for admin
	//note that only the admin can exit the program
	public static void adminMenu(Auditorium[] audlist, Scanner input)
			throws IOException{
		while (true) {
			int response = 0;
			System.out.println("1. Print Report\n2. Logout\n3. Exit\n");
			response = input.nextInt();
			input.reset();
			if (response == 1) 
				printReport(audlist);
			else if (response == 2)
				return;
			else
				exitFunction(audlist);
			}
	}
	
	//writes the state of the auditoriums back to the files and exits
	public static void exitFunction(Auditorium[] audlist) throws IOException {
		audlist[0].writeToFile("A1.txt");
		audlist[1].writeToFile("A2.txt");
		audlist[2].writeToFile("A3.txt");
		
		userMap.forEach((k, v) -> {
			try {
				saveUserOrders(k, v);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		System.exit(0);
	}
	
	//prints a formatted report about the state of the theaters
	public static void printReport(Auditorium[] audlist) {
		System.out.print("Auditorium     Open Seats   Total Reserved Seats   "
				+ "Adult Seats   Senior Seats   Child Seats   Ticket Sales\n");
		double grandTotal = 0;
		int totalOpenSeats=0;
		int totalReservedSeats=0;
		int totalASeats=0;
		int totalCSeats=0;
		int totalSSeats=0;
		for (int i=0; i<3; i++) {
			Auditorium a = audlist[i];
			double total =0;
			int seats = a.getNumCols()*a.getNumRows();
			int openSeats=0;
			int reservedSeats=0;
			int aSeats = 0;
			int cSeats = 0;
			int sSeats = 0;
			TheaterSeat current = a.getFirst();
			if (current.getTicketType() == 'A') {
				totalASeats++;
				aSeats++;
				reservedSeats++;
				total += 10;
			}
			if (current.getTicketType() == 'C') {
				totalCSeats++;
				cSeats++;
				reservedSeats++;
				total += 5;
			}
			if (current.getTicketType() == 'S') {
				totalSSeats++;
				sSeats++;
				reservedSeats++;
				total += 7.5;
			}
			for (int r=0; r<a.getNumRows() - 1; r++) {
				
				for (int j=1; j<a.getNumCols(); j++) {
					current = current.getRight();
					if (current.getTicketType() == 'A') {
						totalASeats++;
						aSeats++;
						reservedSeats++;
						total += 10;
					}
					if (current.getTicketType() == 'C') {
						totalCSeats++;
						cSeats++;
						reservedSeats++;
						total += 5;
					}
					if (current.getTicketType() == 'S') {
						totalSSeats++;
						sSeats++;
						reservedSeats++;
						total += 7.5;
					}
				}
				for (int j=0; j<a.getNumCols() - 1; j++) {
					current = current.getLeft();
				}
				
				current = current.getDown();
				if (current.getTicketType() == 'A') {
					totalASeats++;
					aSeats++;
					reservedSeats++;
					total += 10;
				}
				if (current.getTicketType() == 'C') {
					totalCSeats++;
					cSeats++;
					reservedSeats++;
					total += 5;
				}
				if (current.getTicketType() == 'S') {
					totalSSeats++;
					sSeats++;
					reservedSeats++;
					total += 7.5;
				}
			}
			for (int j=1; j<a.getNumCols(); j++) {
				current = current.getRight();
				if (current.getTicketType() == 'A') {
					totalASeats++;
					aSeats++;
					reservedSeats++;
					total += 10;
				}
				if (current.getTicketType() == 'C') {
					totalCSeats++;
					cSeats++;
					reservedSeats++;
					total += 5;
				}
				if (current.getTicketType() == 'S') {
					totalSSeats++;
					sSeats++;
					reservedSeats++;
					total += 7.5;
				}
			}
			openSeats = seats - reservedSeats;
			totalOpenSeats += openSeats;
			totalReservedSeats += reservedSeats;
			grandTotal += total;
			
			System.out.println("Auditorium " + i + ":      " + openSeats + "                "+
					reservedSeats + "                " + aSeats + "              " 
					+ sSeats + "             " + cSeats + "           $" + total);
			
		}
		System.out.println("Total:             " + totalOpenSeats + "               "+
				totalReservedSeats + "               " + totalASeats + "              " + totalSSeats 
				+ "            " + totalCSeats + "          $" + grandTotal);
	}
	
	//controls flow for regular users
	public static void userMenu(Auditorium[] audlist, Scanner input, String username)
			throws IOException {
		
		while (true) {
			int response = 0;
			System.out.println("1. Reserve Seats\n2. View Orders\n3. Update Order");
			System.out.println("4. Display Receipt\n5. Logout");
			response = input.nextInt();
			input.reset();
			if (response == 1) 
				userReserveSeats(audlist, input, username);
			else if (response == 2)
				userViewOrders(username);
			else if (response == 3)
				userUpdateOrder(username, input, audlist);
			else if (response == 4)
				userDisplayReceipt(username);
			else {
				System.out.println();
				return;
				}
			}
		}
	
	
	//reserves additional seats for users
	public static void userReserveSeats(Auditorium[] audlist, Scanner input,
			String username) {
		int a; //holds auditorium number
		System.out.print("1. Auditorium 1\n2. Auditorium 2\n3. Auditorium 3\n");
		//get auditorium selection from user
		while (true) {
			while (!input.hasNextInt()) {
				System.out.println("Invalid input");
				input.next();
			}
			a = input.nextInt();
			if (a>=1 && a<=3) {
				break;
			} else {
				System.out.println("Invalid auditorium number");
				input.reset();
			}
		}
		a--; //convert to actual auditorium #
		TheaterSeat current = audlist[a].getFirst();
		
		audlist[a].displaySeats();
		//take in relevant variables with input validation
		int rowNum;
		System.out.println("Enter seating row number");
		while (true) {
			while (!input.hasNextInt()) {
				System.out.println("Invalid input");
				input.next();
			}
			rowNum = input.nextInt();
			if (rowNum > 0 && rowNum < audlist[a].getNumRows() + 1) {
				break;
			} else {
				System.out.println("Invalid row number");
				input.reset();
			}
		}
		input.reset();
		
		char seatLetter;
		System.out.println("Enter starting seat letter");
		while (true) {
			
			seatLetter = input.next().charAt(0);
			if (seatLetter > 64 && seatLetter < audlist[a].getNumCols() + 65) {
				break;
			} else {
				System.out.println("Invalid seat letter");
				input.reset();
			}
		}
		input.reset();
		
		int numATickets;
		System.out.println("Enter number of adult tickets");
		while (true) {
			while (!input.hasNextInt()) {
				System.out.println("Invalid input");
				input.next();
			}
			numATickets = input.nextInt();
			if (numATickets >= 0) {
				break;
			} else {
				System.out.println("Invalid number");
				input.reset();
			}
		}
		input.reset();
		
		int numCTickets;
		System.out.println("Enter number of child tickets");
		while (true) {
			while (!input.hasNextInt()) {
				System.out.println("Invalid input");
				input.next();
			}
			numCTickets = input.nextInt();
			if (numCTickets >= 0) {
				break;
			} else {
				System.out.println("Invalid number");
				input.reset();
			}
		}
		input.reset();
		
		int numSTickets;
		System.out.println("Enter number of senior tickets");
		
		while (true) {
			while (!input.hasNextInt()) {
				System.out.println("Invalid input");
				input.next();
			}
			numSTickets = input.nextInt();
			if (numSTickets >= 0) {
				
				break;
			} else {
		
				System.out.println("Invalid number");
				input.reset();
			}
		}

		input.reset();
		//initial user variable input done
		
		char charResponse;
		
		int totalTickets = numATickets + numCTickets + numSTickets;
		//if the seats the user wanted are available, offer a confirmation
		if (audlist[a].checkAvailability(rowNum - 1, seatLetter, totalTickets)){
			System.out.println("The requested seats are available."
					+ " Confirm Reservation? (y/n)");
			while (true) {
				charResponse = input.next().charAt(0);
				if (charResponse == 'y') {
					for (int i =1; i < rowNum; i++) {
						current = current.getDown();
					}
					for (int j=0; j< (int)(seatLetter - 65); j++) {
						current = current.getRight();
					}
					current.bestAvailable = true;
					break;
				} if (charResponse == 'n'){
					break;
				}else if (charResponse != 'y' && charResponse != 'n'){
					System.out.println("Invalid response. "
							+ "Please enter y for yes or n for no");
					input.reset();
				}
			}
			
			input.reset();
			//if the seats are not available, offer up the best available
		} else {
			boolean available = audlist[a].setBestAvailable(totalTickets);
			//if no seats are available, loop back to the start
			if (!available) {
				System.out.println("Sorry, there are no available continuous seats");
				input.reset();
				numSTickets = 0;
				numATickets = 0;
				numCTickets = 0;
				totalTickets = 0;
				return;
			}else {
				audlist[a].displaySeats();
				
				while (true) {
					if (current.bestAvailable) {
						break;
					}
					for (int i=0; i<audlist[a].getNumRows() - 1; i++) {
						
						for (int j=1; j<audlist[a].getNumCols(); j++) {
							current = current.getRight();
							
							if (current.bestAvailable) {
								break;
							}
						}
						if (current.bestAvailable) {
							break;
						}
						
						for (int j=0; j<audlist[a].getNumCols() - 1; j++) {
							
							current = current.getLeft();
						}
					
						current = current.getDown();
						if (current.bestAvailable) {
							break;
						}
					}
					if (current.bestAvailable)
						break;
					for (int j=1; j<audlist[a].getNumCols(); j++) {
						current = current.getRight();
						if (current.bestAvailable) {
							break;
						}
					}
					if (current.bestAvailable)
						break;
				}
				System.out.println("The best available starting seat is row " +
						(current.getRow() + 1) + ", seat " + current.getSeat());
				System.out.println("Reserve these seats? (y/n)");
				while (true) {
					charResponse = input.next().charAt(0);
					if (charResponse == 'y' || charResponse == 'n') {
						break;
					} else {
						System.out.println("Invalid response. "
								+ "Please enter y for yes or n for no");
						input.reset();
					}
				}
				
			}
			
			input.reset();
		}
		if (charResponse == 'y') {
			System.out.println(numATickets);
			System.out.println(numCTickets);
			System.out.println(numSTickets);
			audlist[a].reserveSeats(numATickets, numCTickets, numSTickets);
			//add the order to the user's account
			userMap.get(username).getOrders().add(new Order(a, current.getRow(),
					current.getSeat(), totalTickets, numATickets,
					numCTickets, numSTickets));
			audlist[a].displaySeats();
		}
	current.bestAvailable = false;
	input.reset();	
	numSTickets = 0;
	numATickets = 0;
	numCTickets = 0;
	totalTickets = 0;
	
	}
	
	//display detailed reports of user orders
	public static void userViewOrders(String username) {
		User user = userMap.get(username);
		ArrayList<Order> orders = user.getOrders();
		for (int i=0; i<orders.size(); i++) {
			System.out.println("Order " + (i+1) + ":");
			Order order = orders.get(i);
			System.out.println("Auditorium " + (order.getAuditoriumNum().get(0) + 1));
			System.out.print(order.getRows().get(0) + 1);
			System.out.print(order.getSeats().get(0));
			System.out.print(" ");
			for (int j=1; j<order.getSeats().size(); j++) {
				if (order.getAuditoriumNum().get(j).
						equals(order.getAuditoriumNum().get(j-1))) {
					System.out.print(order.getRows().get(j) + 1);
					System.out.print(order.getSeats().get(j));
					System.out.print(" ");
				} else {
					System.out.println("\nAuditorium " + (order.getAuditoriumNum().get(j) + 1));
					System.out.print(order.getRows().get(j) + 1);
					System.out.print(order.getSeats().get(j));
					System.out.print(" ");
				}
			}
			System.out.println();
			if (order.getNumATickets() == 1) {
				System.out.println(order.getNumATickets() + " Adult Ticket");
			} else if (order.getNumATickets() > 1) {
				System.out.println(order.getNumATickets() + " Adult Tickets");
			}
			if (order.getNumCTickets() == 1) {
				System.out.println(order.getNumCTickets() + " Child Ticket");
			} else if (order.getNumCTickets() > 1) {
				System.out.println(order.getNumCTickets() + " Child Tickets");
			}
			if (order.getNumSTickets() == 1) {
				System.out.println(order.getNumSTickets() + " Senior Ticket");
			} else if (order.getNumSTickets() > 1) {
				System.out.println(order.getNumSTickets() + " Senior Tickets");
			}
			System.out.println();
		}
		
	}
	
	//adds tickets to a user order
	public static void userAddTickets(Auditorium[] audlist, Scanner input,
			String username, int orderNum) {
		int a; //holds auditorium number
		System.out.print("1. Auditorium 1\n2. Auditorium 2\n3. Auditorium 3\n");
		//get auditorium selection from user
		while (true) {
			while (!input.hasNextInt()) {
				System.out.println("Invalid input");
				input.next();
			}
			a = input.nextInt();
			if (a>=1 && a<=3) {
				break;
			} else {
				System.out.println("Invalid auditorium number");
				input.reset();
			}
		}
		a--; //convert to actual auditorium #
		TheaterSeat current = audlist[a].getFirst();
		
		audlist[a].displaySeats();
		//take in relevant variables with input validation
		int rowNum;
		System.out.println("Enter seating row number");
		while (true) {
			while (!input.hasNextInt()) {
				System.out.println("Invalid input");
				input.next();
			}
			rowNum = input.nextInt();
			if (rowNum > 0 && rowNum < audlist[a].getNumRows() + 1) {
				break;
			} else {
				System.out.println("Invalid row number");
				input.reset();
			}
		}
		input.reset();
		
		char seatLetter;
		System.out.println("Enter starting seat letter");
		while (true) {
			
			seatLetter = input.next().charAt(0);
			if (seatLetter > 64 && seatLetter < audlist[a].getNumCols() + 65) {
				break;
			} else {
				System.out.println("Invalid seat letter");
				input.reset();
			}
		}
		input.reset();
		
		int numATickets;
		System.out.println("Enter number of adult tickets");
		while (true) {
			while (!input.hasNextInt()) {
				System.out.println("Invalid input");
				input.next();
			}
			numATickets = input.nextInt();
			if (numATickets >= 0) {
				break;
			} else {
				System.out.println("Invalid number");
				input.reset();
			}
		}
		input.reset();
		
		int numCTickets;
		System.out.println("Enter number of child tickets");
		while (true) {
			while (!input.hasNextInt()) {
				System.out.println("Invalid input");
				input.next();
			}
			numCTickets = input.nextInt();
			if (numCTickets >= 0) {
				break;
			} else {
				System.out.println("Invalid number");
				input.reset();
			}
		}
		input.reset();
		
		int numSTickets;
		System.out.println("Enter number of senior tickets");
		
		while (true) {
			while (!input.hasNextInt()) {
				System.out.println("Invalid input");
				input.next();
			}
			numSTickets = input.nextInt();
			if (numSTickets >= 0) {
				
				break;
			} else {
		
				System.out.println("Invalid number");
				input.reset();
			}
		}

		input.reset();
		//initial user variable input done
		
		char charResponse;
		
		int totalTickets = numATickets + numCTickets + numSTickets;
		//if the seats the user wanted are available, offer a confirmation
		if (audlist[a].checkAvailability(rowNum - 1, seatLetter, totalTickets)){
			System.out.println("The requested seats are available."
					+ " Confirm Reservation? (y/n)");
			while (true) {
				charResponse = input.next().charAt(0);
				if (charResponse == 'y') {
					for (int i =1; i < rowNum; i++) {
						current = current.getDown();
					}
					for (int j=0; j< (int)(seatLetter - 65); j++) {
						current = current.getRight();
					}
					current.bestAvailable = true;
					break;
				} if (charResponse == 'n'){
					break;
				}else if (charResponse != 'y' && charResponse != 'n'){
					System.out.println("Invalid response. "
							+ "Please enter y for yes or n for no");
					input.reset();
				}
			}
			
			input.reset();
			//if the seats are not available, offer up the best available
		} else {
			boolean available = audlist[a].setBestAvailable(totalTickets);
			//if no seats are available, loop back to the start
			if (!available) {
				System.out.println("Sorry, there are no available continuous seats");
				input.reset();
				numSTickets = 0;
				numATickets = 0;
				numCTickets = 0;
				totalTickets = 0;
				return;
			}else {
				audlist[a].displaySeats();
				
				while (true) {
					if (current.bestAvailable) {
						break;
					}
					for (int i=0; i<audlist[a].getNumRows() - 1; i++) {
						
						for (int j=1; j<audlist[a].getNumCols(); j++) {
							current = current.getRight();
							
							if (current.bestAvailable) {
								break;
							}
						}
						if (current.bestAvailable) {
							break;
						}
						
						for (int j=0; j<audlist[a].getNumCols() - 1; j++) {
							
							current = current.getLeft();
						}
					
						current = current.getDown();
						if (current.bestAvailable) {
							break;
						}
					}
					if (current.bestAvailable)
						break;
					for (int j=1; j<audlist[a].getNumCols(); j++) {
						current = current.getRight();
						if (current.bestAvailable) {
							break;
						}
					}
					if (current.bestAvailable)
						break;
				}
				System.out.println("The best available starting seat is row " +
						(current.getRow() + 1) + ", seat " + current.getSeat());
				System.out.println("Reserve these seats? (y/n)");
				while (true) {
					charResponse = input.next().charAt(0);
					if (charResponse == 'y' || charResponse == 'n') {
						break;
					} else {
						System.out.println("Invalid response. "
								+ "Please enter y for yes or n for no");
						input.reset();
					}
				}
				
			}
			
			input.reset();
		}
		if (charResponse == 'y') {
			System.out.println(numATickets);
			System.out.println(numCTickets);
			System.out.println(numSTickets);
			audlist[a].reserveSeats(numATickets, numCTickets, numSTickets);
			//update the order on the user's account
			Order currentOrder = userMap.get(username).getOrders().get(orderNum);
			for (int j=0; j<totalTickets; j++) {
				currentOrder.getAuditoriumNum().add(a);
				currentOrder.getRows().add(current.getRow());
				currentOrder.getSeats().add((char) (current.getSeat() + j));
			}
			currentOrder.setNumATickets(currentOrder.getNumATickets() + numATickets);
			currentOrder.setNumCTickets(currentOrder.getNumCTickets() + numCTickets);
			currentOrder.setNumSTickets(currentOrder.getNumSTickets() + numSTickets);
			currentOrder.setNumTickets(currentOrder.getNumTickets() + totalTickets);
			audlist[a].displaySeats();
		}
	current.bestAvailable = false;
	input.reset();	
	numSTickets = 0;
	numATickets = 0;
	numCTickets = 0;
	totalTickets = 0;
	
	}
	
	//removes a ticket from a user order
	public static void userDeleteTicket(String username, Scanner input,
			Auditorium audlist[], int orderNum) {
		User user = userMap.get(username);
		ArrayList<Order> orders = user.getOrders();
		Order order = orders.get(orderNum);
		//if there is one seat left in the order, cancel it
		if (order.getNumTickets() == 1) {
			user.getOrders().get(orderNum).setNumTickets(0);
			user.getOrders().get(orderNum).setNumATickets(0);
			user.getOrders().get(orderNum).setNumCTickets(0);
			user.getOrders().get(orderNum).setNumSTickets(0);
			user.getOrders().remove(orderNum);
			return;
		}
		int audNum = order.getAuditoriumNum().get(orderNum);
		System.out.println("Enter the row number of the seat you wish to remove");
		int rowNum;
		while (true) {
			while (!input.hasNextInt()) {
				System.out.println("Invalid input");
				input.next();
			}
			rowNum = input.nextInt();
			if (rowNum>=1 && rowNum <= audlist[audNum].getNumRows()) {
				break;
			} else {
				System.out.println("Invalid row number");
				input.reset();
			}
		}
		rowNum--;
		System.out.println("Enter the letter of the seat you wish to remove");
		char seat;
		while (true) {
			while (!input.hasNext()) {
				System.out.println("Invalid input");
				input.next();
			}
			seat = input.next().charAt(0);
			if (seat>=65 && seat <= audlist[audNum].getNumCols() + 65) {
				break;
			} else {
				System.out.println("Invalid auditorium number");
				input.reset();
			}
		}
		
		
		for (int i=0; i<order.getSeats().size(); i++) {
			if (order.getAuditoriumNum().get(i).equals(audNum) 
					&& order.getRows().get(i).equals(rowNum)
					&& order.getSeats().get(i).equals(seat)) {
				Auditorium aud = audlist[audNum];
				TheaterSeat current = aud.getFirst();
				for (int rIndex = 0; rIndex < rowNum; rIndex++) {
					current = current.getDown();
				}
				for (int sIndex = 0; sIndex < (int) (seat - 65); sIndex++) {
					current = current.getRight();
				}
				if (current.getTicketType() == 'A')
					user.getOrders().get(orderNum).setNumATickets(user.getOrders().get(orderNum).getNumATickets() - 1);
				else if (current.getTicketType() == 'C')
					user.getOrders().get(orderNum).setNumCTickets(user.getOrders().get(orderNum).getNumCTickets() - 1);
				else if (current.getTicketType() == 'S')
					user.getOrders().get(orderNum).setNumSTickets(user.getOrders().get(orderNum).getNumSTickets() - 1);
				user.getOrders().get(orderNum).setNumTickets(user.getOrders().get(orderNum).getNumTickets() - 1);
				current.setReserved(false);
				current.setTicketType('.');
				order.getAuditoriumNum().remove(i);
				order.getRows().remove(i);
				order.getSeats().remove(i);
				break;
			}
		}
		
		return;
		
	}
	
	//controls the flow for the update order branch
	public static void userUpdateOrder(String username, Scanner input,
			Auditorium audlist[]) {
		User user = userMap.get(username);
		ArrayList<Order> orders = user.getOrders();
		if (orders.isEmpty()) {
			System.out.println("No orders found");
			return;
		}
		
		userViewOrders(username);
		System.out.println("Enter the number of the order you wish to update");
		int orderNum;
		while (true) {
			while (!input.hasNextInt()) {
				System.out.println("Invalid input");
				input.next();
			}
			orderNum = input.nextInt();
			if (orderNum>=1 && orderNum <= orders.size()) {
				break;
			} else {
				System.out.println("Invalid order number");
				input.reset();
			}
		}
		orderNum--;
		System.out.println("1. Add Tickets to Order");
		System.out.println("2. Delete Tickets from Order");
		System.out.println("3. Cancel Order");
		int selection;
		while (true) {
			while (!input.hasNextInt()) {
				System.out.println("Invalid selection");
				input.next();
			}
			selection = input.nextInt();
			if (selection>=1 && selection <= 3) {
				break;
			} else {
				System.out.println("Invalid selection");
				input.reset();
			}
		}
		if (selection == 1) {
			userAddTickets(audlist, input, username, orderNum);
		} else if (selection == 2) {
			userDeleteTicket(username, input, audlist, orderNum);
		} else {
			user.getOrders().remove(orderNum);
		}
		return;
	}
	
	//shows the user his or her receipt
	public static void userDisplayReceipt(String username) {
		double overallTotal = 0;
		User user = userMap.get(username);
		ArrayList<Order> orders = user.getOrders();
		for (int i=0; i<orders.size(); i++) {
			System.out.println("Order " + (i+1) + ":");
			Order order = orders.get(i);
			System.out.println("Auditorium " + (order.getAuditoriumNum().get(0) + 1));
			System.out.print(order.getRows().get(0) + 1);
			System.out.print(order.getSeats().get(0));
			System.out.print(" ");
			for (int j=1; j<order.getSeats().size(); j++) {
				if (order.getAuditoriumNum().get(j).
						equals(order.getAuditoriumNum().get(j-1))) {
					System.out.print(order.getRows().get(j) + 1);
					System.out.print(order.getSeats().get(j));
					System.out.print(" ");
				} else {
					System.out.println("\nAuditorium " + (order.getAuditoriumNum().get(j) + 1));
					System.out.print(order.getRows().get(j) + 1);
					System.out.print(order.getSeats().get(j));
					System.out.print(" ");
				}
			}
			System.out.println();
			if (order.getNumATickets() == 1) {
				System.out.println(order.getNumATickets() + " Adult Ticket");
			} else if (order.getNumATickets() > 1) {
				System.out.println(order.getNumATickets() + " Adult Tickets");
			}
			if (order.getNumCTickets() == 1) {
				System.out.println(order.getNumCTickets() + " Child Ticket");
			} else if (order.getNumCTickets() > 1) {
				System.out.println(order.getNumCTickets() + " Child Tickets");
			}
			if (order.getNumSTickets() == 1) {
				System.out.println(order.getNumSTickets() + " Senior Ticket");
			} else if (order.getNumSTickets() > 1) {
				System.out.println(order.getNumSTickets() + " Senior Tickets");
			}
			System.out.println("Order Total: $" + order.getTotal() + "\n");
			overallTotal += order.getTotal();
		}
		System.out.println("Overall Total: $" + overallTotal);
		
	}
	
	//reads the username and password file to create a hashmap
	public static HashMap<String, User> readUserData(String fileName) throws FileNotFoundException {
		HashMap<String, User> userMap = new HashMap<String, User>();
		File file = new File(fileName);
		String username;
		String password;
		Scanner userScanner = new Scanner(new FileInputStream(file));
		
		while (userScanner.hasNext()) {
			username = userScanner.next();
			password = userScanner.next();
			
			User temp = new User(password);
			userMap.put(username, temp);
			
		}
		userScanner.close();
		return userMap;
	}
	
	public static void saveUserOrders(String username, User user) throws IOException {
		//orders will be stored like this:
		//username numTickets numATickets numCTickets numSTickets foreach seat: auditoriumNum auditoriumRow seat 
		//loadUserOrders will get users by username and add orders by calling the constructor with the rest of the data on the line
		File writeFile = new File("userOrders.dat");
		BufferedWriter bw = new BufferedWriter(new FileWriter(writeFile, true));
		for (Order o : user.getOrders()) {
			bw.write(username + " ");
			bw.write(o.getNumTickets() + " ");
			bw.write(o.getNumATickets() + " ");
			bw.write(o.getNumCTickets() + " ");
			bw.write(o.getNumSTickets() + " ");
			for (int i = 0; i< o.getNumTickets(); i++) {
				bw.write(o.getAuditoriumNum().get(i) + " ");
				bw.write(o.getRows().get(i) + " ");
				bw.write(o.getSeats().get(i) + " ");
			}
			bw.write("\n");
		}
		bw.close();
	}
	
	public static void loadUserOrders() throws FileNotFoundException {
		File file = new File("userOrders.dat");
		Scanner sc = new Scanner(file);
		while (sc.hasNext()) {
			String username = sc.next();
			int numTickets = sc.nextInt();
			int numATickets = sc.nextInt();
			int numCTickets = sc.nextInt();
			int numSTickets = sc.nextInt();
			User user = userMap.get(username);
			Order order = new Order();
			order.setNumTickets(numTickets);
			order.setNumATickets(numATickets);
			order.setNumCTickets(numCTickets);
			order.setNumSTickets(numSTickets);
			for (int i = 0; i < numTickets; i++) {
				order.getAuditoriumNum().add(sc.nextInt());
				order.getRows().add(sc.nextInt());
				order.getSeats().add(sc.next().charAt(0));
			}
			user.getOrders().add(order);
		}
		sc.close();
	}
	
}


	
	