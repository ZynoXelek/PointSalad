package exceptions;

/**
 * Exception for when there is an error with the configuration.
 */
public class ConfigException extends Exception {
	
	/**
	 * Creates a new ConfigException.
	 */
	public ConfigException() {
		super();
	}

	/**
	 * Creates a new ConfigException with the specified message.
	 * 
	 * @param message
	 */
	public ConfigException(String message) {
		super(message);
	}

	/**
	 * Creates a new ConfigException with the specified message and cause.
	 * 
	 * @param message The message.
	 * @param cause The cause.
	 */
	public ConfigException(String message, Throwable cause) {
		super(message, cause);
	}
}
