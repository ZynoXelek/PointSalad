package java.exceptions;

/**
 * Exception thrown when there is an error setting up the game.
 */
public class SetupException extends Exception {
	
	/**
	 * Creates a new setup exception.
	 */
	public SetupException()
	{
		super();
	}

	/**
	 * Creates a new setup exception with the given message.
	 * 
	 * @param message The message to include in the exception
	 */
	public SetupException(String message)
	{
		super(message);
	}

	/**
	 * Creates a new setup exception with the given message and cause.
	 * 
	 * @param message The message to include in the exception
	 * @param cause The cause of the exception
	 */
	public SetupException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
