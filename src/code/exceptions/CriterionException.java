package code.exceptions;

/**
 * Exception thrown when there is an error with a criterion.
 */
public class CriterionException extends Exception {
	
	/**
	 * Creates a new criterion exception.
	 */
	public CriterionException()
	{
		super();
	}

	/**
	 * Creates a new criterion exception with the given message.
	 * 
	 * @param message The message to include in the exception
	 */
	public CriterionException(String message)
	{
		super(message);
	}

	/**
	 * Creates a new criterion exception with the given message and cause.
	 * 
	 * @param message The message to include in the exception
	 * @param cause The cause of the exception
	 */
	public CriterionException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
