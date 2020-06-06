//Author: Jonathan Harvey
package tickets;

import java.util.ArrayList;
//object for a user order
public class Order {
	//parallel ArrayLists for seats within an order
	private ArrayList<Integer> auditoriumNum = new ArrayList<Integer>();
	private ArrayList<Integer> rows = new ArrayList<Integer>();
	private ArrayList<Character> seats = new ArrayList<Character>();
	private int numTickets;
	private int numATickets;
	private int numCTickets;
	private int numSTickets;
	
	//constructors
	public Order(int auditoriumNum, int auditoriumRow, char startingSeat,
			int numTickets, int numATickets, int numCTickets, int numSTickets) {
		for (int i=0; i<(numTickets); i++) {
			this.auditoriumNum.add(auditoriumNum);
			this.rows.add(auditoriumRow);
			this.seats.add((char) (startingSeat + i));
		}
		this.numTickets = numTickets;
		this.numATickets = numATickets;
		this.numCTickets = numCTickets;
		this.numSTickets = numSTickets;
	}
	
	public Order() {
		
	}
	
	//returns an order total 
	public double getTotal() {
		return numATickets*10+numCTickets*5+numSTickets*7.5;
	}
	
	

	//accessors and mutators
	public ArrayList<Character> getSeats() {
		return seats;
	}

	public ArrayList<Integer> getAuditoriumNum() {
		return auditoriumNum;
	}

	public void setAuditoriumNum(ArrayList<Integer> auditoriumNum) {
		this.auditoriumNum = auditoriumNum;
	}

	public ArrayList<Integer> getRows() {
		return rows;
	}

	public void setRows(ArrayList<Integer> auditoriumRow) {
		this.rows = auditoriumRow;
	}

	public void setSeats(ArrayList<Character> startingSeats) {
		this.seats = startingSeats;
	}


	public int getNumTickets() {
		return numTickets;
	}

	public void setNumTickets(int numTickets) {
		this.numTickets = numTickets;
	}

	public int getNumATickets() {
		return numATickets;
	}

	public void setNumATickets(int numATickets) {
		this.numATickets = numATickets;
	}

	public int getNumCTickets() {
		return numCTickets;
	}

	public void setNumCTickets(int numCTickets) {
		this.numCTickets = numCTickets;
	}

	public int getNumSTickets() {
		return numSTickets;
	}

	public void setNumSTickets(int numSTickets) {
		this.numSTickets = numSTickets;
	}
	
	
	
	
}
