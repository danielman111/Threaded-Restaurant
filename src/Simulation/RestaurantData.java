package Simulation;

public class RestaurantData {
	
	private Address _restaurantAddress;
	private Repository _restaurantRepository;
	private Staff _restaurantStaff;


	/*Behavior*/
	/*Constructors*/
	/**
	 * creates a restaurant data 
	 * @param restaurantAddress - the address of the restaurant
	 * @param restaurantRepository - the repository object of the restaurant
	 * @param restaurantStaff - the staff object of the restaurant
	 */
	public RestaurantData(Address restaurantAddress,Repository restaurantRepository, Staff restaurantStaff) {
		this._restaurantAddress= restaurantAddress;
		this._restaurantRepository= restaurantRepository;
		this._restaurantStaff= restaurantStaff;
	}
	
	/**
	 * Returns the address of the restaurant
	 * @return address of the restaurant
	 */
	public Address getAddress() {
		return this._restaurantAddress;
	}
	
	/**
	 * Returns the repository object of the restaurant
	 * @return repository of the restaurant
	 */
	public Repository getRepository() {
		return this._restaurantRepository;
	}
	
	/**
	 * Returns the staff object of the restaurant
	 * @return repository object of the restaurant
	 */
	public Staff getStaff(){
		return this._restaurantStaff;
	}
	
	/**
	 * Returns the numbers of chefs in the restaurant
	 * @return the numbers of chefs in the restaurant
	 */
	public int getChefSize() {
		return this._restaurantStaff.getChefSize();
	}
	
	/**
	 * Returns the delivery person object of the restaurant
	 * @return the delivery person object of the restaurant
	 */
	public int getDeliveryPersonSize() {
		return this._restaurantStaff.getDeliveryPersonSize();
	}
	
}
