package Simulation;

/**
 * An address
 * @author Daniel Sinaniev, Gal Luvton
 * @version 1.0
 */
public class Address {
	
	private final double _coordinatX;
	private final double _coordinatY;
	
	
	/**
	 *  Constructor
	 *  @param coordinatX the X coordinate of this address
	 *  @param coordinatY the y coordinate of this address
	 */
	public Address(double coordinatX, double coordinatY) {
		this._coordinatX= coordinatX;
		this._coordinatY= coordinatY;
	}
	
	
	/*
	 * Calculates the distance between this address and another given address
	 * @param other  the other address given
	 * @return 		 the distance between the two addresses
	 */
	public double calcDistance(Address other) {
		double x= (this._coordinatX - other._coordinatX);
		double y= (this._coordinatY - other._coordinatY);
		x= x*x;
		y= y*y;
		double ans= Math.sqrt((x+y));
		return ans;
	}
}
