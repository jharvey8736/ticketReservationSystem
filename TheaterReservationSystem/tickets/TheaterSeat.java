//Author: Jonathan Harvey
package tickets;

public class TheaterSeat extends BaseNode{ //seat node with pointers to 4 adjacent nodes
	protected TheaterSeat up;
	protected TheaterSeat down;
	protected TheaterSeat left;
	protected TheaterSeat right;
	boolean bestAvailable = false;
	
	//overloaded constructor that calls BaseNode constructor
	public TheaterSeat(int row, char seat, boolean reserved, char ticketType, TheaterSeat up, TheaterSeat down,
			TheaterSeat left, TheaterSeat right) {
		super(row, seat, reserved, ticketType);
		this.up = up;
		this.down = down;
		this.left = left;
		this.right = right;
	}
	
	
	//accessors and mutators
	public TheaterSeat getUp() {
		return up;
	}

	public void setUp(TheaterSeat up) {
		this.up = up;
	}

	public TheaterSeat getDown() {
		return down;
	}

	public void setDown(TheaterSeat down) {
		this.down = down;
	}

	public TheaterSeat getLeft() {
		return left;
	}

	public void setLeft(TheaterSeat left) {
		this.left = left;
	}

	public TheaterSeat getRight() {
		return right;
	}

	public void setRight(TheaterSeat right) {
		this.right = right;
	}
	
	
	
	
	
	
	
}
