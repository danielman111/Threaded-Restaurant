package Simulation;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * The Main class for this simulation
 * @author Daniel Sinaniev, Gal Luvton
 * @version 1.0
 */
public class Driver {
	
	//the the program starts
	public static long start;
	
	/**
	 * The Main function of this simulation. Parses all the data given and starts the simulation
	 * @param args[0]  the Initial Data xml file
	 * @param args[1]  the Menu xml file
	 * @param args[2]  the Orders List xml file
	 */
	public static void main(String[] args) {
		
		start= System.currentTimeMillis();
		
		RestaurantData restaurantData= ParseXMLDoc.ParseInitialData(args[0], "InitialData.xsd");
		Menu menu= ParseXMLDoc.parseMenu(args[1], "Menu.xsd");
		ArrayList<Order> orders= ParseXMLDoc.parseOrdersList(args[2], "OrdersList.xsd", menu);
		Warehouse warehouse= new Warehouse(restaurantData.getRepository());
		CountDownLatch chefCountdown= new CountDownLatch(restaurantData.getChefSize());
		CountDownLatch deliveryPeopleCountdown= new CountDownLatch(restaurantData.getDeliveryPersonSize());
		Management management= new Management(orders, restaurantData.getStaff(), warehouse, chefCountdown, deliveryPeopleCountdown);
		management.giveOrderToChefs(chefCountdown, deliveryPeopleCountdown);
		
		
		System.out.println("main died");
		System.out.println("******************************************************************");
		System.out.println((System.currentTimeMillis() - start));
				
	}

}
