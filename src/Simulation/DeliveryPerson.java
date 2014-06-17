package Simulation;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

/*
 Found in: Management
Note: Must be Runnable.
This object will hold the current fields: (1) Delivery Person Name (2) Restaurant Address (3)
Speed of Delivery Person (4) Collection of Orders that have been delivered
It will simulate retrieving a new order to deliver as follows:
 Once an order is available for delivery, it is retrieved.
 Calculate distance between the restaurant and customer address, divide by speed. The distance
is calculated as follows: Your restaurant has a pair of coordinates (x1,y1) and each order has a
customer address which also is a pair of coordinates (x2,y2). In order to calculate the distance
between the two points, use Euclidean distance; then round it to the nearest integer. This number
will be in milliseconds. This is the expected delivery time.
 Actual delivery time: the time that was actually spent using Date() polling right when they
receive the delivery item, and after delivery is complete:
 Acquire item to deliver.
 Poll current time.
 Deliver item.
 Poll current time.
 Time that has passed between the rst time polling and the second time polling, is
the time it took for the order to get delivered.
 Sleep distance time - to destination.
 Mark order as delivered. If the (actual cook time + actual delivery time) > 115% * (expected
cook time + expected delivery time), then you receive 50% of the reward, otherwise, you receive
100% of the reward.
 Sleep distance time - to home.
 Repeat cycle. The delivery person will shutdown only when a shutdown request is received.
 */

/**
 * A delivery person. Receives orders and sends them to their destination
 * @author Daniel Sinaniev, Gal Luvton
 * @version 1.0
 */
public class DeliveryPerson implements Runnable {
	
	private final String _name;
	private Address _restaurantAddress;
	private final double _speed;
	private Vector<Order> _ordersDelivered;
    private LinkedBlockingQueue<Order> _deliverBlockingQueue;
    private Statistics _stats;
    private CountDownLatch _signalOnDeath;
    private final static Logger _LOGGER= MyLogger.getLocator().getLogger();

    
    /**
	 *  Constructor
	 *  @param deliveryPersonDetails  an object that holds all the basic details of this delivery person
	 *  @param deliverBlockingQueue	  a blocking queue that is used to send orders to this delivery person
	 *  @param signalOnDeath		  a CountDownLatch used by this delivery person to signal it is done
	 *	@param stats				  a reference to the Statistics object, used to collect data about the orders that were sent
	 *  @see						  MockDeliveryPerson
	 *  @see						  Warehouse
	 *  @see						  CountDownLatch
	 *  @see						  Statistics
	 */
	public DeliveryPerson(MockDeliveryPerson deliveryPersonDetails, LinkedBlockingQueue<Order> deliverBlockingQueue, CountDownLatch signalOnDeath, Statistics stats){
		this._name= deliveryPersonDetails._deliveryPersonName;
		this._ordersDelivered= new Vector<Order>();
		this._restaurantAddress= deliveryPersonDetails._restaurantAddress;
		this._speed= deliveryPersonDetails._speed;
		this._stats= stats;
		this._signalOnDeath= signalOnDeath;
		this._deliverBlockingQueue= deliverBlockingQueue;
	}
	
	
	@Override
	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		while (true) {
			try {
				Order deliverOrder= this._deliverBlockingQueue.take();
				if (deliverOrder == Management.cyanide)
					break;
				logAsBeingDelivered(deliverOrder.getID());
				long timeToDeliverOneWay= calculateDeliveryTime(deliverOrder);
				long timeBeforeDeliver= System.currentTimeMillis();
				Thread.sleep(timeToDeliverOneWay);
				deliverOrder.setStatusAsDelivered(this._name, deliverOrder.getID());
				long timeAfterDeliver= System.currentTimeMillis();
				long actualDeliveryTime= timeAfterDeliver - timeBeforeDeliver;
				long expextedCookTime= deliverOrder.getExpextedCookTime();
				long actualCookTime= deliverOrder.getActualCookTime();
				if(actualCookTime == Order.NULL_COOK_TIME){
					throw new RuntimeException("Order was not cooked!");
				}
				double OrderReward= deliverOrder.calculateOrderReward();
				if((actualCookTime + actualDeliveryTime) > (1.15 * (expextedCookTime + timeToDeliverOneWay))){
					OrderReward= 0.5 * OrderReward;
					deliverOrder.setIfOrderWasDeliveredFastEnough(false);
				}
				else
					deliverOrder.setIfOrderWasDeliveredFastEnough(true);
				Thread.sleep(timeToDeliverOneWay);
				this._ordersDelivered.add(deliverOrder);
				this._stats.updateStatistics(deliverOrder, OrderReward);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this._signalOnDeath.countDown();
	}
	
	
	//adds to the log that an order was taken, with the details of the delivery person
	private void logAsBeingDelivered(int orderID) {
		StringBuilder output= new StringBuilder("Delivery person ");
		output.append(this._name);
		output.append(" took order ");
		output.append(orderID);
		DeliveryPerson._LOGGER.info(output.toString());
	}


	private long calculateDeliveryTime(Order newOrder){
		Address orderAddress=  newOrder.getAddress();
		double distance= this._restaurantAddress.calcDistance(orderAddress);
		long deliveryTime= (long) (distance/this._speed);
		return deliveryTime;
	}
	
	
	
}
