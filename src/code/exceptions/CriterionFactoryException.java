package code.exceptions;

/**
 * Custom exception for criterion factory-related errors.
 */
public class CriterionFactoryException extends Exception {
	
	/**
	 * Creates a new criterion factory exception.
	 */
	public CriterionFactoryException()
	{
		super();
	}

	/**
	 * Creates a new criterion factory exception with the given message.
	 * 
	 * @param message The message to include in the exception
	 */
	public CriterionFactoryException(String message)
	{
		super(message);
	}

	/**
	 * Creates a new criterion factory exception with the given message and cause.
	 * 
	 * @param message The message to include in the exception
	 * @param cause The cause of the exception
	 */
	public CriterionFactoryException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
