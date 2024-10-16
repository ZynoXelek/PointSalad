package code.exceptions;

/**
 * Exception thrown when a scorer encounters an error.
 */
public class ScorerException extends Exception {

	/**
	 * Creates a new scorer exception.
	 */
	public ScorerException()
	{
		super();
	}

	/**
	 * Creates a new scorer exception with the given message.
	 * 
	 * @param message The message to include in the exception
	 */
	public ScorerException(String message)
	{
		super(message);
	}

	/**
	 * Creates a new scorer exception with the given message and cause.
	 * 
	 * @param message The message to include in the exception
	 * @param cause The cause of the exception
	 */
	public ScorerException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
}
