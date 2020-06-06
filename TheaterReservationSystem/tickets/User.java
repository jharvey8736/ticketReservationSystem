//Author: Jonathan Harvey
package tickets;

import java.util.ArrayList;

public class User {
	
	private String password;
	private ArrayList<Order> orders = new ArrayList<Order>();
	
	//constructor that sets the user password
	public User(String password) {
		setPassword(password);
	}
	
	

	//accessors and mutators
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public ArrayList<Order> getOrders() {
		return orders;
	}

	public void setOrders(ArrayList<Order> orders) {
		this.orders = orders;
	}
	
}
