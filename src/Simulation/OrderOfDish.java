package Simulation;
/*Found in: Order
This object will hold information of a dish order.
1
This object will hold: (1) Dish (See 2.1) (2) Quantity (3) Order Status ((i) Incomplete (ii)
InProgress (iii) Complete (Magic Numbers and Solution))
This object will be sent to RunnableCookOneDish objects which will simulate the dish cooking, as
explained below.*/


/**
 * This class represents an order of dish
 * @author Daniel Sinaniev, Gal Luvton
 * @version 1.0
 */

public class OrderOfDish {
	
	private Dish _dish;
	private final int _quantity;
	
	/*Behavior*/
	/*Constructors*/
	/**
	 * creates a new MockChef from a string and two double variables
	 * @param dish - the dish 
	 * @param quantity - the amount of orders of this dish
	 */

	public OrderOfDish(Dish dish, int quantity){
		this._dish= dish;
		this._quantity= quantity;
	}
	
	/**
	 * Returns the difficulty rating of the dish
	 * @return the difficulty rating of the dish in form of a double
	 */
	public double getDifficultyRating() {
		return _dish.getDifficultyRating();
	}
	
	/**
	 * Returns the dish
	 * @return the dish reference
	 */
	public Dish getDish(){
		return _dish;
	}
	
	/**
	 * Returns the quantity of orders of this dish
	 * @return the quantity of orders of this dish in form of int
	 */
	public int getQuantity() {
		return _quantity;
	}
	
	/**
	 * Returns the the expected cook time of the dish
	 * @return the the expected cook time of the dish in form of long
	 */
	public long getExpectedCookTime(){
		return this._dish.getExpectedCookTime();
	}

	/**
	 * Calculates the reward we  get for the current dish
	 * @return the reward we  get for the current dish in form of double
	 */
	public double calculateOrderOfDishReward() {
		double dishReward= this._dish.getReward();
		return (dishReward*this._quantity);
	}
	
}
