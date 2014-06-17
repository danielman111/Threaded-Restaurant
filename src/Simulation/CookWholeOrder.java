package Simulation;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;


/**
 * The cooking process of a whole order
 * @author Daniel Sinaniev, Gal Luvton
 * @version 1.0
 */
public class CookWholeOrder implements Callable<Order> {
	
    //private ExecutorService _dishExecutor;
	private Order _order;
	private Warehouse _warehouse;
	private Chef _cookingChef;
	private Semaphore _signalChef;
	
	
	/**
	 *  Constructor
	 *  @param order		the order that will be cooked by this process
	 *  @param warehouse	a reference to the Warehouse object that holds the tools this process needs
	 *  @param cookingChef  a reference to the chef that started this cooking process
	 *  @see				Simulation#Warehouse
	 *  @see				Simulation#CountDownLatch
	 */
	public CookWholeOrder(Order order, Warehouse warehouse, Chef cookingChef, Semaphore signalChef) {
		this._order= order;
		this._warehouse= warehouse;
		this._cookingChef= cookingChef;
		this._signalChef= signalChef;
	}
	
	
	@Override
	/**
	 * (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	public Order call() throws Exception {
		Thread.currentThread().setName("Order " + this._order.getID());
		long timeBeforeCook= System.currentTimeMillis();
		Vector<OrderOfDish> orderedDishes= this._order.getDishes();
		int numOfUniqeDishes= orderedDishes.size();
		int numOfDishes= this._order.numberOfAllDishes();
		CountDownLatch doneCookingSignal = new CountDownLatch(numOfDishes);
		this._order.setStatus(Order.IN_PROGRESS);
		for(int i=0; i < numOfUniqeDishes; i++){
			Dish currentDish= orderedDishes.elementAt(i).getDish();
			int quantityOfCurrentDish= orderedDishes.elementAt(i).getQuantity();
			for(int j=0; j < quantityOfCurrentDish; j++){
				(new Thread(new CookOneDish(currentDish, this._warehouse, this._cookingChef, doneCookingSignal))).start();
			}
		}
		doneCookingSignal.await();
		long timeAfterCook= System.currentTimeMillis();
		long actualCookTime= timeAfterCook - timeBeforeCook;
		this._order.setActualCookTime(actualCookTime);
		this._order.setStatus(Order.COMPLETE);
		this._signalChef.release();
		return this._order;
	}
	
	
	/**
	 * Returns the difficulty rating of the order being cooked by this process
	 *  @return the difficulty rating of the order being cooked by this process in form of a double
	 */
	public double calculateDifficultyRating(){
		return this._order.getDifficultyRating();
	}

	
	/**
	 * Returns the ID of the order being cooked by this process
	 *  @return the ID of the order being cooked by this process in form of an integer
	 */
	public int getID() {
		return this._order.getID();
	}
	
	
}
