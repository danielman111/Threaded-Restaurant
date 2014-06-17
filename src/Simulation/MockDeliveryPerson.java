package Simulation;


/**
 * This class represents a MockDeliveryPerson. we first Create the MockDeliveryPerson from the 
   xml Parsing so we can know how many delivery people there is, and we can know what is the size of his executor
 * @author Daniel Sinaniev, Gal Luvton
 * @version 1.0
 */
public class MockDeliveryPerson {

	/*Fields*/
	//_deliveryPersonName- the name of the delivery person
	public final String _deliveryPersonName;
	//_restaurantAddress-  the address of the restaurant where the delivery person works
	public final Address _restaurantAddress;
	//_speed- the delivery person speed
	public final double _speed;
	
	/*Behavior*/
	/*Constructors*/
	/**
	 * creates a new MockDeliveryPerson from a string and, Address and a double variable
	 * @param deliveryPersonName - the name of the delivery person
	 * @param restaurantAddress - the Address of the restaurant 
	 * @param speed - the delivery person speed
	 */
	public MockDeliveryPerson(String deliveryPersonName, Address restaurantAddress, double speed) {
		this._deliveryPersonName= deliveryPersonName;
		this._restaurantAddress= restaurantAddress;
		this._speed= speed;
	}

}
