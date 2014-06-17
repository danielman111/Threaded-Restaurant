package Simulation;
import java.util.concurrent.Semaphore;

/**
 * An acquirable kitchen tool
 * @author Daniel Sinaniev, Gal Luvton
 * @version 1.0
 */
public class AcquirableKitchenTool extends KitchenTool{

	private Semaphore _semaphore;
	
	/*
	 *  Constructor
	 *  @param name 	 the name of this tool
	 *  @param quantity  the starting amount of this tool
	 */
	public AcquirableKitchenTool(String name, int quantity) {
		super(name, quantity);
		this._semaphore= new Semaphore(quantity);
	}
	
	
	/*
	 * This method acquires a certain number of tools from the available tool pool
	 *  @param quantity the amount of tools you wish to acquire
	 */
	public void acquire(int quantity) {
		try {
			this._semaphore.acquire(quantity);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	/*
	 * This method returns a certain number of tools to the available tool pool.
	 *  @param quantity the amount of tools you wish to return
	 */
	public void release(int quantity) {
		this._semaphore.release(quantity);
	}

	
}
