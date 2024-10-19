package main.java.exceptions;

/**
 * Exception thrown when an error occurs while drafting cards.
 */
public class DraftingException extends PhaseException {
	
	/**
	 * Creates a new drafting exception.
	 */
	public DraftingException()
	{
		super();
	}

	/**
	 * Creates a new drafting exception with the given message.
	 * 
	 * @param message The message to include in the exception
	 */
	public DraftingException(String message)
	{
		super(message);
	}

	/**
	 * Creates a new drafting exception with the given message and cause.
	 * 
	 * @param message The message to include in the exception
	 * @param cause The cause of the exception
	 */
	public DraftingException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
