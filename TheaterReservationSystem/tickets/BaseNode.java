//Author: Jonathan Harvey
package tickets;

public abstract class BaseNode { //an abstract class for a seat node
	protected int row;
	protected char seat;
	protected boolean reserved;
	protected char ticketType;
	

	//overloaded constructor
	public BaseNode(int row, char seat, boolean reserved, char ticketType) {
		this.row = row;
		this.seat = seat;
		this.reserved = reserved;
		this.ticketType = ticketType;
	}
	
	
	
	//accessors and mutators
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public char getSeat() {
		return seat;
	}
	public void setSeat(char seat) {
		this.seat = seat;
	}
	public boolean isReserved() {
		return reserved;
	}
	public void setReserved(boolean reserved) {
		this.reserved = reserved;
	}
	public char getTicketType() {
		return ticketType;
	}
	public void setTicketType(char ticketType) {
		this.ticketType = ticketType;
	}
	
		
}
