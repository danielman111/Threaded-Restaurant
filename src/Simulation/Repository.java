package Simulation;
import java.util.Vector;


public class Repository {
	
	private Vector<AcquirableKitchenTool> _restKitchenTools;
	private Vector<Ingredient> _restIngredients;
	

	/*Behavior*/
	/*Constructors*/
	/**
	 * creates a new MockChef from a string and two double variables
	 * @param restKitchenTools - a vector that holds the kitchen tools and theirs quantity
	 * @param restIngredients - a vector that holds the ingredients and theirs quantity
	 */
	public Repository(Vector<AcquirableKitchenTool> restKitchenTools, Vector<Ingredient> restIngredients) {
		this._restKitchenTools= restKitchenTools;
		this._restIngredients= restIngredients;
	}
	
	/**
	 * Returns the ingredients vector
	 * @return the ingredients vector
	 */
	public Vector<Ingredient> getIngredients() {
		return this._restIngredients;
	}
	

	/**
	 * Returns the kitchen tool vector
	 * @return the kitchen tool vector
	 */
	public Vector<AcquirableKitchenTool> getKitchenTools(){
		return this._restKitchenTools;
	}
	
}
