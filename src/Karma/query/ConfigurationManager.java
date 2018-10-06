/**
 * 
 */
package Karma.query;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;


/**
 * @author peng
 * Use java property reader to read the configuration
 */
public class ConfigurationManager {
	 private static ConfigurationManager instance = null;
	    private static Properties properties;
	    
	    private ConfigurationManager(InputStream input) {
	        try {
	            properties = new Properties();
	            properties.load(input);
	        } catch (IOException e) {
	            System.err.println("ERROR: Unable to load properties file ");
	            e.printStackTrace(System.err);
	            System.exit(-1);
	        }
	    }
	    
	    public static ConfigurationManager getInstance(InputStream properties) {
	        if (instance == null) {
	            instance = new ConfigurationManager(properties);
	        }
	        return instance;
	    }
	    
	    public String getProperty(String propertyName) {
	        return properties.getProperty(propertyName);
	    }
	    
	    public Object setProperty(String propertyName, String propertyValue) {
	        return properties.setProperty(propertyName, propertyValue);
	    }
	    
	    public void store(OutputStream output, String header){
	    	try {
				properties.store(output, header);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
}
