package Simulation;

import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;


/*
This object contains the following fields: (1) Collection of Orders (2) Collection of Chefs (3) Collection
of Delivery Persons (4) Warehouse.
This department handles two different tasks:
1. Handles adding new orders to the collection of orders to cook.
2. Handles taking new orders from orders to cook and sending them to the appropriate chef in order
to cook them. Once all the chefs are busy, it needs to wait until notfied by a chef, that they are
free to receive new orders to cook.
Be sure not to affect parallelism. If a task is waiting, that doesn't mean the whole application needs to
wait, it means only that task needs to wait, the rest of the application can continue working normally.
 */
public class Management {

	private ArrayList<Chef> _chefs;
	private ArrayList<DeliveryPerson> _deliveryPeople;
	private Warehouse _warehouse;
	private LinkedBlockingQueue<Order> _ordersToDeliveryPersons;
	private ArrayList<Order> _ordersToChefs;
	private ExecutorService _staffExecutor;
	private Statistics _statisticsOfRestaurant;
	private final Object _pillow= new Object();
	private volatile boolean _shouldDoubleCheckOrders;
	public static final Order cyanide= new Order(0, new Vector<OrderOfDish>(), null);
	
	
	public Management(ArrayList<Order> orders, Staff staff, Warehouse warehouse, CountDownLatch chefCountdown, CountDownLatch deliveryPeopleCountdown){
		ArrayList<MockChef> chefDetails= staff.getMockChefs();
		ArrayList<MockDeliveryPerson> deliveryPersonsDetails= staff.getMockDeliveryPersons();
		this._chefs= new ArrayList<Chef>();
		this._deliveryPeople= new ArrayList<DeliveryPerson>();
		this._warehouse= warehouse;
		this._ordersToDeliveryPersons= new LinkedBlockingQueue<Order>();
		this._ordersToChefs= orders;
		this._shouldDoubleCheckOrders= false;
		this._statisticsOfRestaurant= new Statistics();
		this._staffExecutor= Executors.newFixedThreadPool(chefDetails.size());
		for (int i= 0; i < chefDetails.size(); i++) {
			Chef realChef= new Chef(chefDetails.get(i), this._warehouse, this, chefCountdown);
			this._chefs.add(realChef);
			this._staffExecutor.execute(realChef);
		}
		for (int i= 0; i < deliveryPersonsDetails.size(); i++) {
			DeliveryPerson realDeliveryPerson= new DeliveryPerson(deliveryPersonsDetails.get(i), this._ordersToDeliveryPersons, deliveryPeopleCountdown, this._statisticsOfRestaurant);
			this._deliveryPeople.add(realDeliveryPerson);
			this._staffExecutor.execute(realDeliveryPerson);
		}
		this._staffExecutor.shutdown();
	}
	
	
	public void sendOrderToDeliveryPersons(Order finishedOrder) {
		synchronized (this._pillow) {
			try {
				this._ordersToDeliveryPersons.put(finishedOrder);
				this._pillow.notify();
				this._shouldDoubleCheckOrders= true;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public void giveOrderToChefs(CountDownLatch chefCountdown, CountDownLatch deliveryPeopleCountdown) {
		synchronized (this._pillow) {//need to sync pillow in order to wait
			while (this._ordersToChefs.size() > 0) {
				
				/*
				for (int i= 0; i < this._chefs.size(); i++) {
					Chef currentChef= this._chefs.get(i);
					for (int j= 0; (j < this._ordersToChefs.size()); j++) {
						Order currentOrder= this._ordersToChefs.get(j);
						if (currentChef.takeOrderFromManagment(currentOrder)) {
							this._ordersToChefs.remove(currentOrder);
						}
					}
				}
				*/
				
				for (int i= 0; i < this._ordersToChefs.size(); i++) {
					Order currentOrder= this._ordersToChefs.get(i);
					boolean orderSent= false;
					for (int j= 0; ((!orderSent) && (j < this._chefs.size())); j++) {
						if (this._chefs.get(j).takeOrderFromManagment(currentOrder)) {
							this._ordersToChefs.remove(currentOrder);
							orderSent= true;
							i--;
						}
					}
					
					if (!orderSent) {
						try {
							this._pillow.wait();
							i--;
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				
				if (!this._shouldDoubleCheckOrders)// needs to check again if can give orders to chef
					try {
						this._pillow.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				this._shouldDoubleCheckOrders= false;
			}
		}
		try {
			Logger LOGGER= MyLogger.getLocator().getLogger();
			LOGGER.info("Shutting down");
			killChefs();
			chefCountdown.await();
			killDeliveryPersons();
			deliveryPeopleCountdown.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this._statisticsOfRestaurant.printOut();
	}
	
	
	private void killDeliveryPersons() {
		for (int i= 0; i < this._deliveryPeople.size(); i++) {
			try {
				this._ordersToDeliveryPersons.put(Management.cyanide);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	private void killChefs() {
		for (int i= 0; i < this._chefs.size(); i++)
			this._chefs.get(i).killChef();
	}
	
	
	public void printStatistics() {
		this._statisticsOfRestaurant.printOut();
	}
	
	
}
