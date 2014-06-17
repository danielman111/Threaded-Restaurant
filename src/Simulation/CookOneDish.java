package Simulation;
import java.util.concurrent.CountDownLatch;

/*Found in: CallableCookWholeOrder
Note: Must be Runnable.
This object will be our first active object. It contains the following fields: (1) Dish (2) Warehouse
(3) Chef
The purpose of the runnable is to cook a single dish of a given dish order. Each dish instance has a
quantity, and we're going to simulate cooking the whole quantity in parallel. Which means the number
of runnables for each dish equals its quantity.
The life cycle:
 Acquire all the kitchen tools needed for the dish.
 Acquire all the ingredients of the dish.
 Sleep during the cooking time of the dish (multiplied by the chef's eficiency factor, and rounded
to nearest long)
 Return acquired kitchen tools.
Of course, update all applicable variables with new data, such as the status of the Order.*/


/**
 * The cooking process of one dish
 * @author Daniel Sinaniev, Gal Luvton
 * @version 1.0
 */
public class CookOneDish implements Runnable {
	
	private Dish _dish;
	private Warehouse _warehouse;
	private Chef _chef;
	private CountDownLatch _doneCookingThisDishSignal;
	
	
	/**
	 *  Constructor
	 *  @param dish						  a reference to the dish that will be cooked by this process
	 *  @param warehouse				  a reference to the Warehouse object that holds the tools this process needs
	 *  @param chef						  a reference to the chef that started this cooking process
	 *  @param doneCookingThisDishSignal  a CountDownLatch used by this process to signal it is done
	 *  @see							  Warehouse
	 *  @see							  CountDownLatch
	 */
	public CookOneDish(Dish dish, Warehouse warehouse, Chef chef, CountDownLatch doneCookingThisDishSignal){
		this._dish= dish;
		this._warehouse= warehouse;
		this._chef= chef;
		this._doneCookingThisDishSignal= doneCookingThisDishSignal;
	}
	
	
	@Override
	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		this._warehouse.acquaireIngridient(this._dish);
		this._warehouse.acquaireKitchenTool(this._dish);
		long cookingTime= (long)(this._dish.getExpectedCookTime() * this._chef.getChefEfficiencyRating());
		try {
			Thread.sleep(cookingTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this._warehouse.releaseKitchenTool(_dish);
		this._doneCookingThisDishSignal.countDown();
	}
	

}
