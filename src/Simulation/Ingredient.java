package Simulation;
/*This object will hold information of a single ingredient type. Each ingredient contains the following
elds: (1) Name (2) Quantity. This item is consumed in the process. Ingredients used in cooking are
not returned once the cooking is complete.*/

/**
 * An ingredient needed for cooking
 * @author Daniel Sinaniev, Gal Luvton
 * @version 1.0
 */
public class Ingredient {
	
	private final String _name;
	private int _quantity;
	private Object _lock;
	
	
	/*
	 *  Constructor
	 *  @param name 	 the name of this ingredient
	 *  @param quantity  the starting amount of this ingredient
	 */
	public Ingredient(String name, int quantity){
		this._name= name;
		this._quantity= quantity;
		this._lock= new Object();
	}
	

	/*
	 * Tells if the given Ingredient is the same as this Ingredient
	 * @param other  the other ingredient given
	 * @return 		 true if both ingredients are the same, false otherwise
	 */
	public boolean isSameIngredient(Ingredient otherIngredient) {
		return (this._name.equals(otherIngredient._name));
	}
	
	
	/*
	 * Returns the quantity of this ingredient
	 * @return the quantity of this ingredient in the form of an integer
	 */
	public int getQuantity() {
		return this._quantity;
	}
	
	
	/*
	 * Returns the name of this ingredient
	 * @return the name of this ingredient in the form of a String
	 */
	public String getName() {
		return this._name;
	}
	
	
	/*
	 * Adds the given amount to the total amount of this ingredient
	 * @param amountToAdd  the amount that will be added to this ingredient
	 */
	public void addToAmount(int amountToAdd) {
		synchronized (_lock) {
			this._quantity= this._quantity + amountToAdd;
		}
	}
	
	
	/*
	 * This method acquires a certain number of tools from the available ingredient pool
	 * @param quantity  the amount of ingredients you wish to acquire
	 */
	public void acquire(int quantity) {
		synchronized (_lock) {
			this._quantity= this._quantity - quantity;
		}
	}
	
	
	@Override
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder string= new StringBuilder("Ingredient: ");
		string.append(this._name);
		string.append(", Quantity: ");
		string.append(this._quantity);
		return string.toString();
	}
	
	
}	
