package Simulation;
import java.util.Collections;
import java.util.Vector;

/*Found in: Menu
This object will hold information of a single dish. This object can be stored in the restaurant's
menu and can be used in different dish and dish order-related operations.
A Dish object will hold the following information: (1) Dish Name (2) Dish Cook Time (3) Collection
of Dish Ingredients (4) Collection of Required Kitchen Tools (5) Difficulty Rating (6) Reward.
Each dish has different difficulty rating which will affect the distribution of the different Or-
derOfDish to the different chefs. Detailed explanation below. A dish also requires a collection of
exhaustable ingredients and different kitchen tools, in order to successfully simulate the cooking pro-
cedure. */

/**
 * A dish
 * @author Daniel Sinaniev, Gal Luvton
 * @version 1.0
 */
public class Dish {
	
	private final String _name;
	private final long _expextedCookTime;
	private Vector<Ingredient> _requiredIngredients;
	private Vector<KitchenTool> _requiredTools;
	private final double _difficultyRating;
	private final double _reward;
	private boolean _isSorted;
	
	
	/**
	 *  Constructor
	 *  @param name				 the name of this dish
	 *  @param expextedCookTime  the expected time it will take this dish to be cooked
	 *  @param requiredIngs		 all the ingredients needed for this dish
	 *  @param requiredTools	 all the tools needed for this dish
	 *  @param difficultyRating  the difficulty rating of this dish
	 *  @param reward			 the reward given for cooking this dish
	 */
	public Dish(String name, long expextedCookTime, Vector<Ingredient> requiredIngredient, Vector<KitchenTool> requiredTools, double difficultyRating, double reward){
		this._name= name;
		this._expextedCookTime= expextedCookTime;
		this._requiredIngredients= requiredIngredient;
		this._requiredTools= requiredTools;
		this._difficultyRating= difficultyRating;
		this._reward= reward;
		this._isSorted= false;
	}
	
	
	/*
	 * Returns the difficulty rating of this dish
	 * @return the difficulty rating of this dish in form of a double
	 */
	public double getDifficultyRating() {
		return this._difficultyRating;
	}
	
	
	/*
	 * Returns the name of this dish
	 * @return the name of this dish in form of a String
	 */
	public String getName() {
		return this._name;
	}
	
	
	/*
	 * Returns the tools needed for the cooking of this dish
	 * @return a Vector of KitchenTool containing the tools needed for the cooking of this dish
	 */
	public Vector<KitchenTool> getDishTools() {
		return this._requiredTools;
	}
	
	
	/*
	 * Returns the ingredients needed for the cooking of this dish
	 * @return a Vector of Ingredient containing the ingredients needed for the cooking of this dish
	 */
	public Vector<Ingredient> getDishIngredients() {
		return this._requiredIngredients;
	}
	
	
	/*
	 * Sorts the tools required for this dish by alphabetical order
	 */
	public void sortDish() {
		if (!this._isSorted) {
			Collections.sort(this._requiredTools);
			this._isSorted= true;
		}
	}
	
	
	/* Returns the expected cooking time of this dish
	 * @return the expected cooking time of this dish in form of a long
	 */
	public long getExpectedCookTime(){
		return this._expextedCookTime;
	}
	
	
	/* Returns the reward of this dish
	 * @return the reward of this dish in form of a double
	 */
	public double getReward(){
		return this._reward;
	}
	
	
}
