package code.tools;

import java.util.Properties;
import java.io.InputStream;
import java.io.FileInputStream;

import code.exceptions.ConfigException;

/**
 * Singleton class to hold configuration settings for the game.
 */
public class Config {

	/** The path to the configuration file. */
	public static final String CONFIG_PATH = "./src/resources/config.properties";
	private static Config instance = null;

	private Properties properties;
	
	/**
	 * Private constructor to prevent instantiation.
	 * 
	 * @throws ConfigException If there is an error loading the configuration file
	 */
	private Config() throws ConfigException {
		properties = new Properties();
		loadConfig(CONFIG_PATH);
	}

	/**
	 * Gets the instance of the Config class.
	 * 
	 * @return The instance of the Config class
	 * 
	 * @throws ConfigException If there is an error loading the configuration file at instance creation
	 */
	public static Config getInstance() throws ConfigException {
		if (instance == null) {
			instance = new Config();
		}
		return instance;
	}

	/**
	 * Loads the configuration file from the given path.
	 * 
	 * @param configFilepath The path to the configuration file
	 * 
	 * @throws ConfigException If there is an error loading the configuration file
	 */
	public void loadConfig(String configFilepath) throws ConfigException {
		try (InputStream input = new FileInputStream(configFilepath)) {
			properties.load(input);

		} catch (Exception e) {
			throw new ConfigException("Error loading configuration file", e);
		}
	}

    /**
     * Gets a configuration value as a string.
     * 
     * @param key The key of the configuration setting
	 * 
     * @return The value of the configuration setting
     */
    public String getString(String key) {
        return properties.getProperty(key);
    }

    /**
     * Gets a configuration value as an integer.
     * 
     * @param key The key of the configuration setting
	 * 
     * @return The value of the configuration setting as an integer
     */
    public int getInt(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }

    /**
     * Gets a configuration value as a boolean.
     * 
     * @param key The key of the configuration setting
	 * 
     * @return The value of the configuration setting as a boolean
     */
    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(properties.getProperty(key));
    }
}
