package Simulation;
/*This object will hold information of a single kitchen tool type. Each kitchen tool has: (1) Name (2)
Quantity. This item is not consumed in the process, but returned once its use is done.*/

/**
 * A kitchen tool
 * @author Daniel Sinaniev, Gal Luvton
 * @version 1.0
 */
public class KitchenTool implements Comparable<KitchenTool>{
	
	private final String _name;
	private final int _quantity;
	
	
	/**
	 *  Constructor
	 *  @param name 	 the name of this tool
	 *  @param quantity  the starting amount of this tool
	 */
	public KitchenTool(String name, int quantity){
		this._name= name;
		this._quantity= quantity;
	}
	
	
	/**
	 * Tells if the given KitchenTool is the same as this KitchenTool
	 * @param other  the other kitchen tool given
	 * @return 		 true if both kitchen tools are the same, false otherwise
	 */
	public boolean isSameTool(KitchenTool otherTool) {
		return (this._name.equals(otherTool._name));
	}
	
	
	/**
	 * Returns the quantity of this tool
	 * @return the quantity of this tool in the form of an integer
	 */
	public int getQuantity() {
		return this._quantity;
	}
	

	@Override
	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(KitchenTool otherTool) {
		String thisName= this._name;
		String otherName= otherTool._name;
		return thisName.compareTo(otherName);
	}
	
	
	@Override
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder string= new StringBuilder("Tool: ");
		string.append(this._name);
		string.append(", Quantity: ");
		string.append(this._quantity);
		return string.toString();
	}
	
	
}
