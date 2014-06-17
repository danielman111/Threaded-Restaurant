package Simulation;
import java.util.Vector;
import java.util.logging.Logger;
/*This object will hold the information of an order. It will hold (1) Order ID (2) Difficulty Rating (3)
Order Status ((i) Incomplete(ii) InProgress (iii) Complete (iv) Delivered) (4) Collection of
OrderOfDish (5) Customer Address.
Difficullty rating is calculated as a combination of all the difficulties of all the dishes found in the
order.
This object will be sent to CallableCookWholeOrder which will simulate the cooking procedure of
the whole order.
Found in: Management*/


/**
 * This class represents an Order given to the restaurant
 * @author Daniel Sinaniev, Gal Luvton
 * @version 1.0
 */

public class Order {
	
	public static final int INCOMPLETE = 1;
	public static final int IN_PROGRESS = 2;
	public static final int COMPLETE = 3;
	public static final int DELIVERED= 4;
	private final int _orderId;
	private final double _difficultyRating;
	private int _orderStatus;
	private Vector<OrderOfDish> _orderedDishes;
	private final Address _customerAdress;
	private long _actualCookTime;
	private int _wasOrderSentFastEnough;
	public static final int NULL_COOK_TIME= -1;
	private final static Logger _LOGGER= MyLogger.getLocator().getLogger();
	
	/*Behavior*/
	/*Constructors*/
	/**
	 * creates a new MockChef from a string and two double variables
	 * @param _orderId - the order id
	 * @param _orderedDishes - the dishes in the order
	 * @param _customerAdress - the customer address
	 * @param _orderStatus - the order status
	 * @param _wasOrderSentFastEnough - initial value -1, if was sent fast enough 1, else 0
	 * @param _difficultyRating - the order difficulty rating
	 * @param _actualCookTime - the actual cooking time of the order
	 * @see	Address 

	 */
	public Order(int orderId, Vector<OrderOfDish> orderedDishes, Address customerAdress){
		this._orderId= orderId;
		this._orderedDishes= orderedDishes;
		this._customerAdress= customerAdress;
		this._orderStatus= Order.INCOMPLETE;
		this._wasOrderSentFastEnough= -1;
		this._difficultyRating= calculateDifficultyRatingOfOrder();
		this._actualCookTime= Order.NULL_COOK_TIME;
	}
	
	
	
	/**
	 * calculates the difficulty rating of the order
	 * @return the difficulty rating of the order in form of a double
	 */
	private double calculateDifficultyRatingOfOrder() {
		double ans= 0;
		for (int i=0; i < _orderedDishes.size(); i++)
			ans+= (_orderedDishes.elementAt(i)).getDifficultyRating();
		return ans;
	}
	
	
	/**
	 * Returns the difficulty rating of the order
	 * @return the difficulty rating of the order in form of a double
	 */
	public double getDifficultyRating(){
		return _difficultyRating;
	}
	
	/**
	 * Returns a copy of the dishes vector 
	 * @return a copy of the dishes vector
	 */
	public Vector<OrderOfDish> getDishes(){
		Vector<OrderOfDish> orderedDishes= new Vector<OrderOfDish>();
		int numofdishes= _orderedDishes.size();
		for(int i=0; i < numofdishes; i++){
			orderedDishes.addElement(_orderedDishes.get(i));
		}
		return orderedDishes;
	}
	
	/**
	 * calculates the expected cook time 
	 * @return the expected cook time in a form of long
	 */
	public long getExpextedCookTime(){
		long expectedCookTime= 0;
		for (int i=0; i < this._orderedDishes.size(); i++) {
			long dishCookTime= this._orderedDishes.elementAt(i).getExpectedCookTime();
			if(dishCookTime > expectedCookTime)
				expectedCookTime= dishCookTime;
		}
		return expectedCookTime;
	}	
		
	/**
	 * Returns the actual cook time 
	 * @return the actual cook time in form of a long, if the order was not cooked returns -1 
	 */
	public long getActualCookTime() {
		if(this._actualCookTime != -1){
			return this._actualCookTime;
		}
		else
			return -1;
	}
		
	/**
	 * Returns the actual cook time 
	 * @return the actual cook time in a form of a double, if the order was not cooked returns -1 
	 */
	public double calculateOrderReward(){
		double orderReward= 0;
		for(int i=0; i < this._orderedDishes.size(); i++){
			orderReward += this._orderedDishes.elementAt(i).calculateOrderOfDishReward();
		}
		return orderReward;
	}
	
	/**
	 * sets the order status to the given status 
	 */
	public void setStatus(int status) {
		this._orderStatus= status;
		StringBuilder output= new StringBuilder("Order ");
		output.append(this._orderId);
		if (this._orderStatus == Order.IN_PROGRESS)
			output.append(" is now in progress");
		if (this._orderStatus == Order.COMPLETE)
			output.append(" is complete");
		Order._LOGGER.info(output.toString());
	}
	
	/**
	 * sets the order status to be complete            wtf- status as delivered to be complete?
	 * @param deliveryPersonName-  the name of the delivery person who delivers the order
	 * @param orderID- the order id 
	 */
	public void setStatusAsDelivered(String deliveryPersonName, int orderID) {
		this._orderStatus= Order.DELIVERED;
		StringBuilder output= new StringBuilder("Order ");
		output.append(orderID).append(" was delivered by ");
		output.append(deliveryPersonName);
		Order._LOGGER.info(output.toString());
	}
	
	/**
	 * Returns the customer address 
	 * @return the address of the costumer who ordered the order in an Address form
	 */
	public Address getAddress(){
		return this._customerAdress;
		
	}

	/**
	 * sets the actual cook time of the order to the given cook time 
	 * @exception RuntimeException if the order already have an actual cook time.	 
	 * @param actualCookTime- the actual cook time of the order
	 */
	public void setActualCookTime(long actualCookTime) {
		if(this._actualCookTime == Order.NULL_COOK_TIME){
			this._actualCookTime= actualCookTime;
		}
		else{
			throw new RuntimeException("Order was allready assign with cook time!");
		}
	}
	

	/**
	 * Returns the customer address 
	 * @return the address of the costumer who ordered the order in form of int
	 */
	public int getID() {
		return this._orderId;
	}

	/**
	 * Returns the number of the dishes in the order 
	 * @return the number of the dishes in the order, in form of a int
	 */
	public int numberOfAllDishes() {
		int numOfAllDishes= 0;
		for (int i= 0; i < this._orderedDishes.size(); i++){
			numOfAllDishes+= this._orderedDishes.elementAt(i).getQuantity();
		}
		return numOfAllDishes;
	}
	
	/**
	 * sets the order fast delivery boolean variable to be 1 or 0 by the given flag 
	 * @param flag- a boolean variable indicates if the order was delivered fast enough
	 */
	public void setIfOrderWasDeliveredFastEnough(boolean flag) {
		if (flag)
			this._wasOrderSentFastEnough= 1;
		else
			this._wasOrderSentFastEnough= 0;
	}
	
	/**
	 * Returns the true or false by the boolean variable that indicates if the order fast send fast enough 
	 * @return true if the order was sent fast enough, false otherwise
	 */
	public boolean wasOrderDeliveredFastEnough() {
		if (1 == this._wasOrderSentFastEnough)
			return true;
		if (0 == this._wasOrderSentFastEnough)
			return false;
		else
			throw new RuntimeException("Order 'delivery on time' status was not set!");
	}
	
	/**
	 * Returns the string information of this class 
	 * @return a string with the form: 
	 * 		"Order id: "
	 * 		"Order expected cook time: "
	 * 		"Order actual cook time: "
	 * 		"Dishes in order: "
	 * 		 quantity " orders of " dish name
	 * 		"Expected reward from order: "
	 * 		"Actual reward from order: "
	 */
	public String toString() {
		StringBuilder output= new StringBuilder("Order id: ");
		output.append(this._orderId);
		output.append(System.getProperty("line.separator"));
		output.append("Order expected cook time: ");
		output.append(getExpextedCookTime());
		output.append(System.getProperty("line.separator"));
		output.append("Order actual cook time: ");
		output.append(this._actualCookTime);
		output.append(System.getProperty("line.separator"));
		output.append("Dishes in order: ");
		output.append(System.getProperty("line.separator"));
		for (int i= 0; i < this._orderedDishes.size(); i++) {
			output.append("	");
			output.append(this._orderedDishes.elementAt(i).getQuantity());
			output.append(" orders of ");
			output.append(this._orderedDishes.elementAt(i).getDish().getName());
			output.append(System.getProperty("line.separator"));
		}
		double orderReward= this.calculateOrderReward();
		output.append("Expected reward from order: ");
		if (1 == this._wasOrderSentFastEnough)
			output.append(orderReward);
		if (0 == this._wasOrderSentFastEnough)
			output.append(orderReward*0.5);
		output.append(System.getProperty("line.separator"));
		output.append("Actual reward from order: ");
		output.append(orderReward);
		return (output.toString());
	}
	
	 
}
