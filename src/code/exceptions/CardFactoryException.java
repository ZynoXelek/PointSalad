package code.exceptions;

/**
 * Custom exception for card factory-related errors.
 */
public class CardFactoryException extends Exception {
	
	/**
	 * Creates a new card factory exception.
	 */
	public CardFactoryException()
	{
		super();
	}

	/**
	 * Creates a new card factory exception with the given message.
	 * 
	 * @param message The message to include in the exception
	 */
	public CardFactoryException(String message)
	{
		super(message);
	}

	/**
	 * Creates a new card factory exception with the given message and cause.
	 * 
	 * @param message The message to include in the exception
	 * @param cause The cause of the exception
	 */
	public CardFactoryException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
