package Simulation;
import java.util.ArrayList;


public class Staff {
	
	
	private ArrayList<MockChef>_restChefs;
	private ArrayList<MockDeliveryPerson> _restDeliveryPeople;
	
	/*Behavior*/
	/*Constructors*/
	/**
	 * creates a new MockChef from a string and two double variables
	 * @param restChefs - a ArrayList of MockChef
	 * @param restDeliveryPeople - a ArrayList of MockDeliveryPerson
	 */
	public Staff(ArrayList<MockChef> restChefs, ArrayList<MockDeliveryPerson> restDeliveryPeople) {
		this._restChefs= restChefs;
		this._restDeliveryPeople= restDeliveryPeople;
	}
	
	/**
	 * Returns the ArrayList of a MockChef
	 * @return the ArrayList of a MockChef
	 */
	public ArrayList<MockChef> getMockChefs(){
		return this._restChefs;
	}
	
	/**
	 * Returns the ArrayList of a MockDeliveryPerson
	 * @return the ArrayList of a MockDeliveryPerson
	 */
	public ArrayList<MockDeliveryPerson> getMockDeliveryPersons() {
		return this._restDeliveryPeople;
	}

	/**
	 * Returns the number of chef's in the restaurant
	 * @return the number of chef's in the restaurant in a form of int
	 */
	public int getChefSize() {
		return (this._restChefs.size());
	}
	
	/**
	 * Returns the number of delivery persons in the restaurant
	 * @return the number of delivery persons in the restaurant in a form of int
	 */
	public int getDeliveryPersonSize() {
		return (this._restDeliveryPeople.size());
	}
	
}
