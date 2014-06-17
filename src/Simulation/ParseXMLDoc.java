package Simulation;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * This class containts static function the parse the xml files
 * @author Daniel Sinaniev, Gal Luvton
 * @version 1.0
 */
public class ParseXMLDoc {

	/*Behavior*/
	/*Constructors*/
	/**
	 *  empty constructor
	 */
	public ParseXMLDoc() {	
	}
	
	/**
	 * parses the initial data file
	 * @param filePath- the path of the of the file
	 * @param XSDFilePath-  the scheme path
	 * @see- RestaurantData
	 * @return the restaurant data
	 */
	public static RestaurantData ParseInitialData(String filePath, String XSDFilePath){
		try {
			DocumentBuilderFactory documentBuilder = DocumentBuilderFactory.newInstance();
			documentBuilder.setIgnoringElementContentWhitespace(true);
			Schema schema = SchemaFactory.newInstance(
			                XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new File(XSDFilePath));
			documentBuilder.setSchema(schema);
			Document doc = documentBuilder.newDocumentBuilder().parse(filePath);
			doc.getDocumentElement().normalize();
			Address restaurantAddress= parseInitialDataAddress(doc);
			Repository restaurantRepository= parseInitialDataRepository(doc);
			Staff restaurantStaff= parseInitialDataStaff(doc, restaurantAddress);
			RestaurantData newRestaurantData= new RestaurantData(restaurantAddress, restaurantRepository, restaurantStaff);
			return newRestaurantData;  
		}       
		catch (Exception e) {
			System.out.println(e.getMessage());
	    }
		throw (new RuntimeException("error while parsing the initialdata!"));
	}
	
	
	private static Address parseInitialDataAddress(Document doc){
		NodeList addressList = doc.getElementsByTagName("Address");
		Node addressNode = addressList.item(0);
		Address restaurantAdress;
		if (addressNode.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) addressNode;
			double x= Double.parseDouble(eElement.getElementsByTagName("x").item(0).getTextContent());
			double y= Double.parseDouble(eElement.getElementsByTagName("y").item(0).getTextContent());
			restaurantAdress= new Address(x,y);
			return restaurantAdress;
		}
		throw (new RuntimeException("error while parsing the address"));
		
	}
	
	private static Repository parseInitialDataRepository(Document doc){
		Vector<AcquirableKitchenTool> RestKitchenTools= new Vector<AcquirableKitchenTool>();
		NodeList kitchenToolsList = doc.getElementsByTagName("KitchenTool");
		for (int j = 0; j < kitchenToolsList.getLength(); j++) {
			Node kitchenToolsNode = kitchenToolsList.item(j);
			parseToolsForInitialDataRepository(kitchenToolsNode, RestKitchenTools);
		}
		if (RestKitchenTools.size() == 0)
			throw (new RuntimeException("RestKitchenTools is empty!"));
		NodeList IngredientList = doc.getElementsByTagName("Ingredient");
		Vector<Ingredient> RestIngredients= new Vector<Ingredient>();
		for (int j = 0; j < IngredientList.getLength(); j++) {
			Node IngredientNode = IngredientList.item(j);
			parseIngredientsForInitialDataRepository(IngredientNode, RestIngredients);
		}
		if (RestIngredients.size() == 0)
			throw (new RuntimeException("RestKitchenTools is empty!"));
		Repository respo= new Repository(RestKitchenTools, RestIngredients);
		return respo;
	}
	
	
	private static void parseToolsForInitialDataRepository(Node kitchenToolsNode, Vector<AcquirableKitchenTool> RestKitchenTools) {
		if (kitchenToolsNode.getNodeType() == Node.ELEMENT_NODE) {
			Element kitchenToolsElement = (Element) kitchenToolsNode;
			String toolName= kitchenToolsElement.getElementsByTagName("name").item(0).getTextContent();
			int toolQuantity= Integer.decode(kitchenToolsElement.getElementsByTagName("quantity").item(0).getTextContent());
			if((toolName == null) || (toolName == ""))
				throw (new RuntimeException("toolName is null or empty string!"));
			if(toolQuantity < 0)
				throw (new RuntimeException("toolQuantity is negetive!"));
			AcquirableKitchenTool newKitchenTool= new AcquirableKitchenTool(toolName, toolQuantity);
			RestKitchenTools.add(newKitchenTool);
		}
	}
	
	
	private static void parseIngredientsForInitialDataRepository(Node IngredientNode, Vector<Ingredient> RestIngredients) {
		if (IngredientNode.getNodeType() == Node.ELEMENT_NODE) {
			Element IngredientsElement = (Element) IngredientNode;
			String ingredientName= IngredientsElement.getElementsByTagName("name").item(0).getTextContent();
			int ingredientQuantity= Integer.decode(IngredientsElement.getElementsByTagName("quantity").item(0).getTextContent());
			if((ingredientName == null) || (ingredientName == ""))
				throw (new RuntimeException("ingredientName is null or empty string!"));
			if(ingredientQuantity < 0)
				throw (new RuntimeException("toolQuantity is negetive!"));
			Ingredient newIngredient= new Ingredient(ingredientName, ingredientQuantity);
			RestIngredients.add(newIngredient);
		}
	}
	
	
	private static Staff parseInitialDataStaff(Document doc, Address restAddress){
		NodeList ChefList = doc.getElementsByTagName("Chef");
		ArrayList<MockChef> RestChefs= new ArrayList<MockChef>();
		for (int j = 0; j < ChefList.getLength(); j++) {
			Node ChefNode = ChefList.item(j);
			if (ChefNode.getNodeType() == Node.ELEMENT_NODE) {
				Element ChefElement = (Element) ChefNode;
				String chefName= ChefElement.getElementsByTagName("name").item(0).getTextContent();
				double chefEfficiencyRating= Double.parseDouble(ChefElement.getElementsByTagName("efficiencyRating").item(0).getTextContent());
				double chefEnduranceRating= Double.parseDouble(ChefElement.getElementsByTagName("enduranceRating").item(0).getTextContent());
				MockChef newChef= new MockChef(chefName,chefEfficiencyRating, chefEnduranceRating);
				RestChefs.add(newChef);
			}
		}
		NodeList deliveryPersonList = doc.getElementsByTagName("DeliveryPerson");
		ArrayList<MockDeliveryPerson> RestDeliveryPeople= new ArrayList<MockDeliveryPerson>();
		for (int j = 0; j < deliveryPersonList.getLength(); j++) {
			Node deliveryPersonNode = deliveryPersonList.item(j);
			Element deliveryPersonElement = (Element) deliveryPersonNode;
			if (deliveryPersonNode.getNodeType() == Node.ELEMENT_NODE) {
				String deliveryPersonName= deliveryPersonElement.getElementsByTagName("name").item(0).getTextContent();
				double deliveryPersonSpeed= Double.parseDouble(deliveryPersonElement.getElementsByTagName("speed").item(0).getTextContent());
				MockDeliveryPerson newDevPerson= new MockDeliveryPerson(deliveryPersonName, restAddress, deliveryPersonSpeed);
				RestDeliveryPeople.add(newDevPerson);
			}
		}
		Staff newStaff= new Staff(RestChefs, RestDeliveryPeople);
		return newStaff;
	}
	
	/**
	 * parses the initial menu file
	 * @param filePath- the path of the of the file
	 * @param XSDFilePath-  the scheme path
	 * @see- RestaurantData
	 * @return the menu object
	 */
	public static Menu parseMenu(String filePath, String XSDFilePath){
		try {
			DocumentBuilderFactory documentBuilder = DocumentBuilderFactory.newInstance();
			documentBuilder.setIgnoringElementContentWhitespace(true);
			Schema schema = SchemaFactory.newInstance(
			                XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new File(XSDFilePath));
			documentBuilder.setSchema(schema);
			Document doc = documentBuilder.newDocumentBuilder().parse(filePath);
			doc.getDocumentElement().normalize();
			Vector<Dish> collectionOfDish= new Vector<Dish>();	
			NodeList DishList = doc.getElementsByTagName("Dish");
			for (int i = 0; i < DishList.getLength(); i++) {
				Node DishNode = DishList.item(i);
				parseOneDishOfMenu(DishNode, collectionOfDish);
			}
			if (collectionOfDish.size() == 0)
				throw (new RuntimeException("collectionOfDish is empty!"));
			Menu menu= new Menu(collectionOfDish);
			return menu;
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		throw (new RuntimeException("error while parsing the orders"));
	}
	
	
	private static void parseOneDishOfMenu(Node DishNode, Vector<Dish> collectionOfDish) {
		Vector<Ingredient> requiredDishIngredient= new Vector<Ingredient>();
		Vector<KitchenTool> requiredDishTools= new Vector<KitchenTool>();
		if (DishNode.getNodeType() == Node.ELEMENT_NODE) {
			Element orderElement = (Element) DishNode;
			String dishName= orderElement.getElementsByTagName("name").item(0).getTextContent();
			double dishDifficultyRating= Double.parseDouble(orderElement.getElementsByTagName("difficultyRating").item(0).getTextContent());
			long expextedDishCookTime= Long.parseLong(orderElement.getElementsByTagName("expectedCookTime").item(0).getTextContent());
			double dishReward= Double.parseDouble(orderElement.getElementsByTagName("reward").item(0).getTextContent());
			NodeList KitchenToolList = orderElement.getElementsByTagName("KitchenTool");
			parseMenuKitchenTools(KitchenToolList, requiredDishTools);
			NodeList IngredientList = orderElement.getElementsByTagName("Ingredient");
			parseMenuIngredient(IngredientList, requiredDishIngredient);
			Dish currentDish= new Dish(dishName, expextedDishCookTime, requiredDishIngredient, requiredDishTools, dishDifficultyRating, dishReward);
			collectionOfDish.add(currentDish);
		}
	}
	
	
	private static void parseMenuKitchenTools(NodeList KitchenToolList, Vector<KitchenTool> requiredDishTools) {
		for(int j=0; j < KitchenToolList.getLength(); j++){
			Node KitchenToolNode = KitchenToolList.item(j);
			if (KitchenToolNode.getNodeType() == Node.ELEMENT_NODE) {
				Element toolElement = (Element) KitchenToolNode;
				String toolName= toolElement.getElementsByTagName("name").item(0).getTextContent();
				int toolQuantity= Integer.decode(toolElement.getElementsByTagName("quantity").item(0).getTextContent());
				KitchenTool tempKitchenTool= new KitchenTool(toolName, toolQuantity);
				requiredDishTools.add(tempKitchenTool);
			}
		}
	}
	
	
	private static void parseMenuIngredient(NodeList IngredientList, Vector<Ingredient> requiredDishIngredient) {
		for(int j=0; j < IngredientList.getLength(); j++){
			Node IngredientNode = IngredientList.item(j);
			if (IngredientNode.getNodeType() == Node.ELEMENT_NODE) {
				Element IngredientElement = (Element) IngredientNode;
				String ingredientName= IngredientElement.getElementsByTagName("name").item(0).getTextContent();
				int ingredientQuantity= Integer.decode(IngredientElement.getElementsByTagName("quantity").item(0).getTextContent());
				Ingredient tempIngredient= new Ingredient(ingredientName, ingredientQuantity);
				requiredDishIngredient.add(tempIngredient);
			}
		}
	}
	
	/**
	 * parses the initial order list file
	 * @param filePath- the path of the of the file
	 * @param XSDFilePath-  the scheme path
	 * @see- RestaurantData
	 * @return an array list of an order
	 */
	public static ArrayList<Order> parseOrdersList(String filePath, String XSDFilePath, Menu menu){
		try {
			DocumentBuilderFactory documentBuilder = DocumentBuilderFactory.newInstance();
			documentBuilder.setIgnoringElementContentWhitespace(true);
			Schema schema = SchemaFactory.newInstance(
			                XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new File(XSDFilePath));
			documentBuilder.setSchema(schema);
			Document doc = documentBuilder.newDocumentBuilder().parse(filePath);
			doc.getDocumentElement().normalize();
			ArrayList<Order> orderCollection= new ArrayList<Order>();
			
			NodeList orderList = doc.getElementsByTagName("Order");
			for (int j = 0; j < orderList.getLength(); j++) {
				Node orderNode = orderList.item(j);
				if (orderNode.getNodeType() == Node.ELEMENT_NODE) {
					Element orderElement = (Element) orderNode;
					Order oneOrder= parseSingleOrderFromOrdersList(orderElement, menu);
					orderCollection.add(oneOrder);
				}
			}
			return orderCollection;
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
	    }
		throw (new RuntimeException("error while parsing the orders"));
	}
	
	
	private static Order parseSingleOrderFromOrdersList(Element orderElement, Menu menu) {
		Vector<OrderOfDish> orderedDishes= new Vector<OrderOfDish>();
		int orderId= Integer.decode(((orderElement.getAttribute("id")).toString()));
		NodeList addressList = orderElement.getElementsByTagName("DeliveryAddress");
		Node addressNode = addressList.item(0);
		Address orderAddress= new Address(0, 0);
		if (addressNode.getNodeType() == Node.ELEMENT_NODE) {
			Element addressElement = (Element) addressNode;
			double addressX= Double.parseDouble(addressElement.getElementsByTagName("x").item(0).getTextContent());
			double addressY= Double.parseDouble(addressElement.getElementsByTagName("y").item(0).getTextContent());
			orderAddress= new Address(addressX, addressY);
		}
		NodeList dishList = orderElement.getElementsByTagName("Dish");
		for (int i= 0; i < dishList.getLength(); i++){
			Node dishNode = dishList.item(i);
			if (dishNode.getNodeType() == Node.ELEMENT_NODE) {
				Element dishElement = (Element) dishNode;
				String dishName= dishElement.getElementsByTagName("name").item(0).getTextContent();
				int dishQuantity= Integer.decode(dishElement.getElementsByTagName("quantity").item(0).getTextContent());
				Dish currentDish= menu.findDishByName(dishName);
				if (currentDish == null)
					throw (new RuntimeException("no such dish as " + dishName));
				OrderOfDish orderedOneDish= new OrderOfDish(currentDish, dishQuantity);
				orderedDishes.add(orderedOneDish);
			}
		}
		return new Order(orderId, orderedDishes, orderAddress);
	}
	
	
}

		
