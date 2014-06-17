package Simulation;
import java.util.Vector;

/*Found in: Management
This object contains: (1) a collection of available kitchen tools (2) a collection of available ingre-
dients. This warehouse is the shared storage component where the different chefs acquire their tools
and needed ingredients from.
You may assume the warehouse contains enough ingredients and kitchen tools in order to success-
fully complete the simulation process.*/



/**
 * This class represents the warehouse of the restaurant
 * @author Daniel Sinaniev, Gal Luvton
 * @version 1.0
 */
public class Warehouse {
	
	private Vector<Ingredient> _availableIngredients;
	private Vector<AcquirableKitchenTool> _availableTools;

	/*Behavior*/
	/*Constructors*/
	/**
	 * @param repository- an object that contains the available ingredients and tools
	 */
	public Warehouse(Repository repository){
		this._availableIngredients= repository.getIngredients(); 
		this._availableTools= repository.getKitchenTools();
	}

	/**
	 * acquaires the ingredients for the dish given
	 * @param dish-  the dish we need to acquire the ingredients for
	 */
	public void acquaireIngridient(Dish dish) {
		Vector<Ingredient> dishIngredients= dish.getDishIngredients();
		for (int i= 0; i < dishIngredients.size(); i++) {
			boolean found= false;
			int location= 0;
			for (int j= 0; ((!found) && (j < this._availableIngredients.size())); j++) {
				found= (this._availableIngredients.elementAt(j)).isSameIngredient(dishIngredients.elementAt(i));
				if (found)
					location= j;
			}
			int dishIngredientQuantity= dishIngredients.elementAt(i).getQuantity();
			this._availableIngredients.elementAt(location).acquire(dishIngredientQuantity);
		}
	}

	
	/**
	 * acquaires the kitchen tools for the dish given
	 * @param dish-  the dish we need to acquire the kitchen tools for
	 */
	public void acquaireKitchenTool(Dish dish) {
		dish.sortDish();
		Vector<KitchenTool> dishTools= dish.getDishTools();
		for (int i= 0; i < dishTools.size(); i++) {
			boolean found= false;
			int location= 0;
			for (int j= 0; ((!found) && (j < this._availableTools.size())); j++) {
				found= (this._availableTools.elementAt(j)).isSameTool(dishTools.elementAt(i));
				if (found)
					location= j;
			}
			int dishToolQuantity= dishTools.elementAt(i).getQuantity();
			this._availableTools.elementAt(location).acquire(dishToolQuantity);
		}
	}

	/**
	 * acquaires the kitchen tools for the dish given
	 * @param dish-  the dish we need to acquire the kitchen tools for
	 */
	public void releaseKitchenTool(Dish dish) {
		Vector<KitchenTool> dishTools= dish.getDishTools();
		for (int i= 0; i < dishTools.size(); i++) {
			boolean found= false;
			int location= 0;
			for (int j= 0; ((!found) && (j < this._availableTools.size())); j++) {
				found= (this._availableTools.elementAt(j)).isSameTool(dishTools.elementAt(i));
				if (found)
					location= j;
			}
			int dishToolQuantity= dishTools.elementAt(i).getQuantity();
			this._availableTools.elementAt(location).release(dishToolQuantity);
		}
	}

}
