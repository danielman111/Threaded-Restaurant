package Simulation;
import java.util.Vector;

/*
 * Found in: Management
This object contains the following fields:
1. Money Gained: Money gained from delivering orders. For each order, If the (actual cook time +
actual delivery time) < 1.15 * (expected cook time + expected delivery time), then you receive
100% of the reward, otherwise, you receive 50% of the reward.
2. Delivered Orders: Collection of delivered orders, including their information.
3. Collection of Ingredients consumed and their quantity.
Updating this object may be done from anywhere in the program, however it is advised to do so in
minimal amount of places.
 */

/**
 * This class containts the statistics informatin of the restaurant
 * @author Daniel Sinaniev, Gal Luvton
 * @version 1.0
 */
public class Statistics {
	
	private double _moneyGained;
	private Vector<Order> _deliveredOrders;
	private Vector<Ingredient> _consumedIngredients;
	private Object _lock;
	
	/*Behavior*/
	/*Constructors*/
	public Statistics() {
		this._moneyGained= 0;
		this._deliveredOrders= new Vector<Order>();
		this._consumedIngredients= new Vector<Ingredient>();
		this._lock= new Object();
	}
	
	/**
	 * updates the total reward so far, and the ingredients vector
	 * @param orderDelivered-  an order that was delivered, we add it to the delivered orders vector
	 * @param orderReward- the reward we got from the order
	 */
	public void updateStatistics(Order orderDelivered, double orderReward) {
		synchronized (_lock) {
			this._moneyGained+= orderReward;
			this._deliveredOrders.add(orderDelivered);
			Vector<OrderOfDish> orderOfDishes= orderDelivered.getDishes();
			for (int i= 0; i < orderOfDishes.size(); i++)
				updateConsumedIngredients(orderOfDishes.elementAt(i));
		}
	}
	
	/**
	 * updates the ingredient vector, with the ingredients that was consumed with the given orderOfdish
	 * @param dishesOrdered-  the order of dish, that we get the information about the ingredients
	 * @see OrderOfDish
	 */
	private void updateConsumedIngredients(OrderOfDish dishesOrdered) {
		int multiplier= dishesOrdered.getQuantity();
		Vector<Ingredient> orderedDish= (dishesOrdered.getDish()).getDishIngredients();
		for (int i= 0; i < orderedDish.size(); i++) {
			Ingredient dishIngredient= orderedDish.elementAt(i);
			int location= findIngredientInConsumedList(dishIngredient);
			if (location < 0)
				this._consumedIngredients.add(new Ingredient(dishIngredient.getName(), dishIngredient.getQuantity()*multiplier));
			else {
				this._consumedIngredients.elementAt(location).addToAmount(dishIngredient.getQuantity()*multiplier);
			}
		}
	}
	
	/**
	 * Returns the place of the given ingredient in the vector, if not exits there return -1
	 * @param ingredient-  the ngredient we are looking for, in the consumed ingredients vector 
	 * @return the place of the ingredients, if not exits return -1
	 */
	private int findIngredientInConsumedList(Ingredient ingredient) {
		for (int i= 0; i < this._consumedIngredients.size(); i++)
			if (this._consumedIngredients.elementAt(i).isSameIngredient(ingredient))
				return i;
		return -1;
	}

	/**
	 * prints the statistics
	 */
	public void printOut() {
		StringBuilder output= new StringBuilder(System.getProperty("line.separator"));
		output.append("The total money gained is: ");
		output.append(this._moneyGained);
		output.append(System.getProperty("line.separator"));
		output.append("The orders were: ");
		output.append(System.getProperty("line.separator"));
		output.append(System.getProperty("line.separator"));
		for (int i= 0; i < this._deliveredOrders.size(); i++) {
			output.append(this._deliveredOrders.elementAt(i).toString());
			output.append(System.getProperty("line.separator"));
			output.append(System.getProperty("line.separator"));
		}
		output.append("The ingredients used were: ");
		output.append(System.getProperty("line.separator"));
		output.append(System.getProperty("line.separator"));
		for (int i= 0; i < this._consumedIngredients.size(); i++) {
			output.append(this._consumedIngredients.elementAt(i).toString());
			output.append(System.getProperty("line.separator"));
		}
		MyLogger.getLocator().getLogger().info(output.toString());
		MyLogger.getLocator().CloseHander();
	}
	
}
