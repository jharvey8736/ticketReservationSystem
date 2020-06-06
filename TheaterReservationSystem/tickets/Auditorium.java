//Author: Jonathan Harvey
package tickets;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Auditorium {
	//alphabet for printing purposes
	final static char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
			'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
	TheaterSeat first; //first object in the linked list
	int numRows;
	int numCols;
	
	//constructor - builds the grid
	public Auditorium(String filename) throws FileNotFoundException {
		//node variables
		int row;
		char seat; 
		boolean reserved; 
		char ticketType;
		TheaterSeat up = null;
		TheaterSeat down = null;
		TheaterSeat left = null;
		TheaterSeat right = null;
		
		//read from the file passed to the constructor
		//and fill the grid with that data
		File seatingFile = new File(filename);
		Scanner seatScanner = new Scanner(new FileInputStream(seatingFile));
		String firstLine = seatScanner.nextLine();
		numRows = 1;
		numCols = firstLine.length();
		String fullInput = firstLine;
		
		while (seatScanner.hasNext()) {
			fullInput += seatScanner.nextLine();
			numRows++;
		}
		seatScanner.close();
		
		TheaterSeat previous = null;
		row = 0;
		seat = 'A';
		ticketType = fullInput.charAt(0);
		if (ticketType == '.') {
			reserved = false;
		} else {
			reserved = true;
		}
		first = new TheaterSeat(row, seat, reserved, ticketType, up, down, left, right);
		TheaterSeat current = getFirst();
		int sIndex = 0;
		int rIndex = 0;
		int location = 0;
		for (rIndex = 0; rIndex < numRows - 1; rIndex++) { //iterate through grid
			row = rIndex;
			for (sIndex = 0; sIndex < numCols - 1; sIndex++) {
				
				//preserve previous node
				previous = current;
				
				seat = (char) (sIndex + 65);
				location = (1+rIndex)*(numCols) + sIndex;
				ticketType = fullInput.charAt(location);
				if (ticketType == '.') {
					reserved = false;
				} else {
					reserved = true;
				}
				
				current = new TheaterSeat(row + 1, seat, reserved, ticketType, up, down, left, right);
				//create up-down link
				previous.setDown(current);
				current.setUp(previous);
				
				seat = (char) (sIndex + 66);
				location = rIndex*numCols + sIndex + 1;
				ticketType = fullInput.charAt(location);
				if (ticketType == '.') {
					reserved = false;
				} else {
					reserved = true;
				}
				
				current = new TheaterSeat(row, seat, reserved, ticketType, up, down, left, right);
				//create right-left link
				current.setLeft(previous);
				previous.setRight(current);
			}
			
			previous = current;
			
			seat = (char) (sIndex + 66);
			location = (1 + rIndex)*numCols + sIndex;
			if (ticketType == '.') {
				reserved = false;
			} else {
				reserved = true;
			}
			
			current = new TheaterSeat(row, seat, reserved, ticketType, up, down, left, right);
			//create up-down link
			previous.setDown(current);
			current.setUp(previous);
			
			current = previous;
			for (int index = 0; index < numCols - 1; index++) {
				current = current.getLeft();
				
			}
			current = current.getDown();
		} //end of for loop
		
		for (sIndex = 0; sIndex < numCols - 1; sIndex++) {
		previous = current;
		seat = (char) (sIndex + 66);
		location = (numRows - 1)*numCols + sIndex + 1;
		ticketType = fullInput.charAt(location);
		if (ticketType == '.') {
			reserved = false;
		} else {
			reserved = true;
		}
		
		current = new TheaterSeat(numRows - 1, seat, reserved, ticketType, up, down, left, right);
		//create right-left link
		current.setLeft(previous);
		previous.setRight(current);
		}
	} //end of constructor
	

	
	
	//displays the seats to the user
	public void displaySeats() {
		System.out.print("  ");
		for (int i =  0; i<numCols; i++) {
			System.out.print(alphabet[i]);
		}
		System.out.println();
		TheaterSeat current = getFirst();
		
		for (int i=0; i<numRows - 1; i++) {
			System.out.print((i + 1) + " ");
			for (int j=0; j<numCols; j++) {
				
				System.out.print(current.getTicketType());
				if (current.getRight()!=null)
					current = current.getRight();
			}
			for (int j=0; j<numCols - 1; j++) {
				current = current.getLeft();
			}
			
			System.out.println();
			current = current.getDown();
		}
		System.out.print(numRows + " ");
		for (int j=0; j<numCols; j++) {
			
			System.out.print(current.getTicketType());
			if (current.getRight()!=null)
				current = current.getRight();
		}
		System.out.println();
	}
	
	
	//returns true if the seats are available, false if not
	public boolean checkAvailability(int row, char seat, int tickets) {
		TheaterSeat current = getFirst();
		for (int rIndex = 0; rIndex < row; rIndex++) {
			current = current.getDown();
		}
		for (int sIndex = 0; sIndex < (int) (seat - 65); sIndex++) {
			current = current.getRight();
		}
		for (int index = 0; index < tickets; index++) {
			if (current.getTicketType() != '.') {
				return false;
			}
			current = current.getRight();
		}
		return true;
	}
	
	//switches the bestAvailable flag to true on the
	//first seat of the sequence of best available ones
	public boolean setBestAvailable(int tickets) {
		TheaterSeat previous = null;
		double closeness = 9999;
		double tempCloseness = 9999;
		int middle = tickets / 2;
		int toRight;
		if (middle % 2 == 0) {
			toRight = middle - 1;
		} else {
			toRight = middle;
		}
		
		TheaterSeat current = getFirst();
		
		for (int i=0; i<numRows; i++) {
			for (int j=0; j< numCols - tickets; j++) {
				
			
				if (checkAvailability(i, (char)(j + 65), tickets)) {
					for (int i1 = 0; i1<middle; i1++) {
						current = current.getRight();
					}
					//distance formula w/o square root to find relative distance from center
					double xSquared = Math.pow(numCols/2.0 - .5 -(int)(current.getSeat() - 65), 2);
					double ySquared = Math.pow(numRows/2.0 - .5 - (int)(current.getRow()), 2);
					tempCloseness = xSquared + ySquared;
					System.out.println(current.getRow());
					for (int i1 = 0; i1<middle; i1++) {
						current = current.getLeft();
					}
					System.out.println(tempCloseness);
					
					if (tempCloseness < closeness) {
						closeness = tempCloseness;
						current.bestAvailable = true;
						if (previous != null)
							previous.bestAvailable = false;
						previous = current;
					} else if (tempCloseness == closeness) {
						//find closest row to center
						double newYDistanceSquare = Math.pow(current.getRow() - numRows/2.0 + .5, 2);
						double oldYDistanceSquare = Math.pow(previous.getRow() - numRows/2.0 + .5, 2);
						System.out.println("here");
						System.out.println(newYDistanceSquare);
						System.out.println(oldYDistanceSquare);
						//favors lower row in case of tie (must be less than oldYDistanceSqaure)
						if (newYDistanceSquare < oldYDistanceSquare) {
							closeness = tempCloseness;
							current.bestAvailable = true;
							if (previous != null)
								previous.bestAvailable = false;
							previous = current;
						}
					}
				
					
				}
				current = current.getRight();
			}
			//go back to the left and down a row
			for (int i2 = 0; i2 < numCols - tickets; i2++) {
				current = current.getLeft();
			}
			current = current.getDown();
		}
		if (closeness == 9999) {
			return false; //shows no seats passed the availability test (very high #)
		}
		return true;
	}
	
	
	//changes ticket type and reserved boolean for nodes that are to be reserved
	public void reserveSeats(int aTickets, int cTickets, int sTickets) {
		//iterates through the grid similar to displaySeats,
		//applying the same if (current.bestAvailable)
		//block to every node
		TheaterSeat current = getFirst();
		
		if (current.bestAvailable) {
			for (int aIndex = 0; aIndex < aTickets; aIndex++) {
				
				current.setTicketType('A');
				current.setReserved(true);
				if (current.getRight()!=null)
					current = current.getRight();
			}
			for (int cIndex = 0; cIndex < cTickets; cIndex++) {
				
				current.setTicketType('C');
				current.setReserved(true);
				if (current.getRight()!=null)
					current = current.getRight();
			}
			for (int sIndex = 0; sIndex < sTickets; sIndex++) {
				
				current.setTicketType('S');
				current.setReserved(true);
				if (current.getRight()!=null)
					current = current.getRight();
			}
			return; //return from the function once the best has been set
		}
		
		for (int i=0; i<numRows - 1; i++) {
			
			for (int j=0; j<numCols - 1; j++) {
				current = current.getRight();
				if (current.bestAvailable) {
					for (int aIndex = 0; aIndex < aTickets; aIndex++) {
						current.setTicketType('A');
						current.setReserved(true);
						if (current.getRight()!=null)
						current = current.getRight();
					}
					for (int cIndex = 0; cIndex < cTickets; cIndex++) {
						current.setTicketType('C');
						current.setReserved(true);
						if (current.getRight()!=null)
						current = current.getRight();
					}
					for (int sIndex = 0; sIndex < sTickets; sIndex++) {
						current.setTicketType('S');
						current.setReserved(true);
						if (current.getRight()!=null)
						current = current.getRight();
					}
					return;
				}
			}
			for (int j=0; j<numCols - 1; j++) {
				current = current.getLeft();
			}
			current = current.getDown();
			if (current.bestAvailable) {
				for (int aIndex = 0; aIndex < aTickets; aIndex++) {
					current.setTicketType('A');
					current.setReserved(true);
					if (current.getRight()!=null)
					current = current.getRight();
				}
				for (int cIndex = 0; cIndex < cTickets; cIndex++) {
					current.setTicketType('C');
					current.setReserved(true);
					if (current.getRight()!=null)
					current = current.getRight();
				}
				for (int sIndex = 0; sIndex < sTickets; sIndex++) {
					current.setTicketType('S');
					current.setReserved(true);
					if (current.getRight()!=null)
					current = current.getRight();
				}
				return;
			}
			
		}
		for (int j=0; j<numCols - 1; j++) {
			current = current.getRight();
			if (current.bestAvailable) {
				for (int aIndex = 0; aIndex < aTickets; aIndex++) {
					current.setTicketType('A');
					current.setReserved(true);
					if (current.getRight()!=null)
					current = current.getRight();
				}
				for (int cIndex = 0; cIndex < cTickets; cIndex++) {
					current.setTicketType('C');
					current.setReserved(true);
					if (current.getRight()!=null)
					current = current.getRight();
				}
				for (int sIndex = 0; sIndex < sTickets; sIndex++) {
					current.setTicketType('S');
					current.setReserved(true);
					if (current.getRight()!=null)
					current = current.getRight();
				}
				return;
			}
		}
	}
	
	//writes the contents of the grid to a file
	public void writeToFile(String file) throws IOException {
		String body = ""; //everything is stored in a string which
		//is written to the file
		File writeFile = new File(file);
		BufferedWriter audWriter = new BufferedWriter(new FileWriter(writeFile));
		TheaterSeat current = getFirst();
		body += current.getTicketType();
		for (int i=0; i<numRows - 1; i++) {
			
			for (int j=1; j<numCols; j++) {
				current = current.getRight();
				body += current.getTicketType();
			}
			for (int j=0; j<numCols - 1; j++) {
				current = current.getLeft();
			}
			
			body += "\n";
			current = current.getDown();
			body += current.getTicketType();
		}
		for (int j=1; j<numCols; j++) {
			current = current.getRight();
			body += current.getTicketType();
		}
		audWriter.write(body);
		audWriter.close();
	}
	
	//displays a report for ticket sales
	public void displayReport() {
		int ticketsSold = 0;
		int aTicketsSold = 0;
		int cTicketsSold = 0;
		int sTicketsSold = 0;
		double totalSales = 0;
		
		TheaterSeat current = getFirst();
		if (current.getTicketType() == 'A') {
			aTicketsSold++;
			ticketsSold++;
			totalSales += 10;
		}
		if (current.getTicketType() == 'C') {
			cTicketsSold++;
			ticketsSold++;
			totalSales += 5;
		}
		if (current.getTicketType() == 'S') {
			sTicketsSold++;
			ticketsSold++;
			totalSales += 7.5;
		}
		for (int i=0; i<numRows - 1; i++) {
			
			for (int j=1; j<numCols; j++) {
				current = current.getRight();
				if (current.getTicketType() == 'A') {
					aTicketsSold++;
					ticketsSold++;
					totalSales += 10;
				}
				if (current.getTicketType() == 'C') {
					cTicketsSold++;
					ticketsSold++;
					totalSales += 5;
				}
				if (current.getTicketType() == 'S') {
					sTicketsSold++;
					ticketsSold++;
					totalSales += 7.5;
				}
			}
			for (int j=0; j<numCols - 1; j++) {
				current = current.getLeft();
			}
			
			current = current.getDown();
			if (current.getTicketType() == 'A') {
				aTicketsSold++;
				ticketsSold++;
				totalSales += 10;
			}
			if (current.getTicketType() == 'C') {
				cTicketsSold++;
				ticketsSold++;
				totalSales += 5;
			}
			if (current.getTicketType() == 'S') {
				sTicketsSold++;
				ticketsSold++;
				totalSales += 7.5;
			}
		}
		for (int j=1; j<numCols; j++) {
			current = current.getRight();
			if (current.getTicketType() == 'A') {
				aTicketsSold++;
				ticketsSold++;
				totalSales += 10;
			}
			if (current.getTicketType() == 'C') {
				cTicketsSold++;
				ticketsSold++;
				totalSales += 5;
			}
			if (current.getTicketType() == 'S') {
				sTicketsSold++;
				ticketsSold++;
				totalSales += 7.5;
			}
		}
		System.out.println("Total seats in theater: " + numRows*numCols);
		System.out.println("Total tickets sold: " + ticketsSold);
		System.out.println("Adult tickets sold: " + aTicketsSold);
		System.out.println("Child tickets sold: " + cTicketsSold);
		System.out.println("Senior tickets sold: " + sTicketsSold);
		System.out.println("Total ticket sales: $" + String.format("%.2f", totalSales));
	}


//accessors and mutators
public TheaterSeat getFirst() {
	return first;
}

public void setFirst(TheaterSeat first) {
	this.first = first;
}




public int getNumRows() {
	return numRows;
}




public void setNumRows(int numRows) {
	this.numRows = numRows;
}




public int getNumCols() {
	return numCols;
}




public void setNumCols(int numCols) {
	this.numCols = numCols;
}

}




