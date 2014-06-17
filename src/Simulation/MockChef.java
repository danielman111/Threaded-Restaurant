package Simulation;


/**
 * This class represents a MockChef. we first Create the MockChef from the xml Parsing so we can know 
   how many chef's there is, and we can know what is the size of his executor
 * @author Daniel Sinaniev, Gal Luvton
 * @version 1.0
 */

public class MockChef {

	/*Fields*/
	//_chefName- the name of the chef
	public final String _chefName;
	//_chefEfficiencyRating- the efficiency rating of the chef 
	public final double _chefEfficiencyRating;
	//_chefEnduranceRating- the endurance rating of the chef 
	public final double _chefEnduranceRating;
	
	
	
	/*Behavior*/
	/*Constructors*/
	/**
	 * creates a new MockChef from a string and two double variables
	 * @param chefName - the name of the chef
	 * @param chefEfficiencyRating - the efficiency rating of the chef
	 * @param chefEnduranceRating - the endurance rating of the chef
	 */
	public MockChef(String chefName, double chefEfficiencyRating, double chefEnduranceRating) {
		this._chefName= chefName;
		this._chefEfficiencyRating= chefEfficiencyRating;
		this._chefEnduranceRating= chefEnduranceRating;
	}
	

}
