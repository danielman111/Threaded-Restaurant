package Simulation;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

/*
Found in: Management
Note: Must be Runnable.
This object is our third active object. Fields: (1) Chef Name (2) Chef Eciency Rating (3)
Endurance Rating (4) Current Pressure (5) Collection of Futures for Orders in Progress (6) Pool of
Threads.
This runnable receives new cook orders from the Managment, and depending on dish difficulty,
endurance rating and current pressure, decides whether to accept or deny the request for cooking the
order. The formula is as follows: if (Dish Diculty < Endurance Rating - Current Pressure) then the
request is accepted, and the cooking simulation procedure automatically begins. Of course, the value
of current pressure needs to be increased by the value of dish diculty.
3
At any time, the chef may receive a shut down request. If received, the chef will nish cooking the
orders he may be working on while denying new requests, once all currently being cooked orders are
complete, the thread exits.
Once an Order is nished, the current pressure value needs to be decreased by the value of the
completed Order diculty. Then the chef sends the nished Order to the delivery department queue.
 */
 
 
 /**
 * A chef. Cooks orders on demand
 * @author Daniel Sinaniev, Gal Luvton
 * @version 1.0
 */
public class Chef implements Runnable {
	
	private final String _chefName;
	private final double _chefEfficiencyRating;
	private final double _chefEnduranceRating;
	private double _currentPressure;
	private Warehouse _warehouse;
	private Management _management;
    private ArrayList<Future<Order>> _futuresOfOrder;
    private ExecutorService _cookWholeOrderExecutor;
    private CountDownLatch _signalOnDeath;
	private boolean _managmentSentDeathSignal;
	private LinkedBlockingQueue<Order> _incomingOrders;
	private ExecutorCompletionService<Order> _cookWholeOrderCompletion;
	private Semaphore _orderFinishedSignal;
	private final static Logger _LOGGER= MyLogger.getLocator().getLogger();
	
	
	/**
	 *  Constructor
	 *  @param chefDetails	  an object that holds all the basic details of this Chef
	 *  @param warehouse  	  a reference to the Warehouse object that holds the tools this Chef needs
	 *  @param management	  a reference to the Management object used that holds this Chef
	 *  @param signalOnDeath  a CountDownLatch used by this Chef to signal it is done
	 *  @see				  MockChef
	 *  @see				  Warehouse
	 *  @see				  Managment
	 *  @see				  CountDownLatch
	 */
	public Chef(MockChef chefDetails, Warehouse warehouse, Management management, CountDownLatch signalOnDeath) {
		this._chefName= chefDetails._chefName;
		this._chefEfficiencyRating= chefDetails._chefEfficiencyRating;
		this._chefEnduranceRating= chefDetails._chefEnduranceRating;
		this._futuresOfOrder= new ArrayList<Future<Order>>();
		this._currentPressure= 0;
		this._warehouse= warehouse;
		this._management= management;
		this._signalOnDeath= signalOnDeath;
		this._managmentSentDeathSignal= false;
		this._incomingOrders= new LinkedBlockingQueue<Order>();
		this._orderFinishedSignal= new Semaphore(0);
		this._cookWholeOrderExecutor= Executors.newCachedThreadPool();
		this._cookWholeOrderCompletion = new ExecutorCompletionService<Order>(this._cookWholeOrderExecutor);
	}

	
	@Override
	/**
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		Thread.currentThread().setName(_chefName);
		while ((!this._managmentSentDeathSignal) || (!this._incomingOrders.isEmpty()) || (!this._futuresOfOrder.isEmpty())) {
			try {
				this._orderFinishedSignal.acquire();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			if (!this._incomingOrders.isEmpty()) {
				Order orderToCook= null;
				try {
					orderToCook= this._incomingOrders.take();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				CookWholeOrder newWholeOrder= new CookWholeOrder(orderToCook, this._warehouse, this, this._orderFinishedSignal);
				StringBuilder output= new StringBuilder("Chef ");
				output.append(this._chefName).append(" took order ").append(newWholeOrder.getID());
				Chef._LOGGER.info(output.toString());
				Future<Order> currentFuture= this._cookWholeOrderCompletion.submit(newWholeOrder);
				this._futuresOfOrder.add(currentFuture);
			}
			else if (!this._futuresOfOrder.isEmpty()){
				Order orderSentToCooking= null;
				Future<Order> futureOfOrderSentToCooking= null;
				try {
					futureOfOrderSentToCooking = this._cookWholeOrderCompletion.take(); //waits until has order done
					orderSentToCooking= futureOfOrderSentToCooking.get();
				} catch (Exception e) {
					e.printStackTrace();
				}
				this._futuresOfOrder.remove(futureOfOrderSentToCooking);
				double orderDifficulty = orderSentToCooking.getDifficultyRating();
				this._management.sendOrderToDeliveryPersons(orderSentToCooking);
				this._currentPressure= this._currentPressure - orderDifficulty;
			}		
		}
		this._cookWholeOrderExecutor.shutdown();
		this._signalOnDeath.countDown();
	}

	
	/**
	 * Receives the difficulty rating of an order and determines if this chef is able to cook it
	 * @param difficultyRating  the difficulty rating of an order
	 * @return					true if this chef can cook it, false otherwise
	 */
	public boolean canCookOrder(double difficultyRating){
		return (difficultyRating <= (this._chefEnduranceRating - this._currentPressure));
	}
	
	
	/**
	 * Receives an order. If this chef can cook the order, it starts cooking it and returns true , otherwise, returns false.
	 * @param newOrder  the order in this chef needs to cook
	 * @return			true if this chef can cook it, false otherwise
	 */
	public boolean takeOrderFromManagment(Order newOrder) {
		double orderDifficulty= newOrder.getDifficultyRating();
		boolean canCook= canCookOrder(orderDifficulty);
		if (canCook) {
			this._currentPressure= this._currentPressure + orderDifficulty;
			try {
				this._incomingOrders.put(newOrder);
				this._orderFinishedSignal.release();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return canCook;
	}
	
	
	/**
	 * Returns this chef's efficiency rating
	 * @return  this chef's efficiency rating
	 */
	public double getChefEfficiencyRating(){
		return this._chefEfficiencyRating;
	}
	
	
	/**
	 * Tells the chef that no more orders are coming
	 */
	public void killChef() {
		this._managmentSentDeathSignal= true;
		this._orderFinishedSignal.release();
	}
	
	
}
