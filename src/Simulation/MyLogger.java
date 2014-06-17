package Simulation;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * A logger class, using the singleton pattern
 * @author Daniel Sinaniev, Gal Luvton
 * @version 1.0
 */
public class MyLogger {

    private static MyLogger locator = new MyLogger();
    private Logger logger;
    private FileHandler handler;

    
    private MyLogger() {
    	initLogger();
    }

    
    /**
     * Returns the logger's locator
     * @return Pointer to the logger
     */
    public static MyLogger getLocator(){
    	return locator;
    }

    
    /**
     * Returns the logger
     * @return logger
     */
    public Logger getLogger() {
    	return logger;
    }

   
    private void initLogger() {
    	logger = Logger.getLogger("Logger");
    	logger.setLevel(Level.INFO);
		try {
			handler= new FileHandler("Logging.txt");
			handler.setFormatter(new SimpleFormatter());//handler.setFormatter(new PlainTextFormat());
			logger.addHandler(handler);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    
    /**
     * Closes the logger's handler
     */
    public void CloseHander() {
    	handler.close();
    }
    
}
