package Simulation;
import java.util.Vector;

/**
 * A menu of our restaurant
 * @author Daniel Sinaniev, Gal Luvton
 * @version 1.0
 */

public class Menu {
	
	private Vector<Dish> _dishes;
	
	/*Behavior*/
	/*Constructors*/
	/**
	 * creates a new MockChef from a string and two double variables
	 * @param dishes - a vector that contains all the dishes in the menu
	 */
	public Menu(Vector<Dish> dishes){
		_dishes= dishes;
	}
	
	
	/**
	 * Returns the dish Object with a given dish Name
	 * @param dishName - a name of a Dish
	 * @return the vector of a MockChef
	 */
	public Dish findDishByName(String dishName) {
		for (int i= 0; i < _dishes.size(); i++){
			Dish currentDish= _dishes.elementAt(i);
			if ((currentDish.getName()).equals(dishName))
				return currentDish;
		}
		return null;
	}
	
}
