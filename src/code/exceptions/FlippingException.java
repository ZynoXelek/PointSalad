package code.exceptions;

/**
 * Exception thrown when an error occurs during the flipping phase.
 */
public class FlippingException extends PhaseException {
	
	/**
	 * Creates a new flipping exception.
	 */
	public FlippingException()
	{
		super();
	}

	/**
	 * Creates a new flipping exception with the given message.
	 * 
	 * @param message The message to include in the exception
	 */
	public FlippingException(String message)
	{
		super(message);
	}

	/**
	 * Creates a new flipping exception with the given message and cause.
	 * 
	 * @param message The message to include in the exception
	 * @param cause The cause of the exception
	 */
	public FlippingException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
