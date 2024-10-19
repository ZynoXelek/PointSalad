package exceptions;

/**
 * Exception thrown when an error occurs in the bot logic.
 */
public class BotLogicException extends Exception {

	/**
	 * Creates a new BotLogicException.
	 */
	public BotLogicException() {
		super();
	}

	/**
	 * Creates a new BotLogicException with the given message.
	 * 
	 * @param message The message to include in the exception.
	 */
	public BotLogicException(String message) {
		super(message);
	}

	/**
	 * Creates a new BotLogicException with the given cause.
	 * 
	 * @param cause The cause of the exception.
	 */
	public BotLogicException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
