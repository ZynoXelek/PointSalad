package main.java.exceptions;

/**
 * Exception thrown when there is an error during the scoring phase.
 */
public class ScoringException extends PhaseException {
	
	/**
	 * Creates a new scoring exception.
	 */
	public ScoringException()
	{
		super();
	}

	/**
	 * Creates a new scoring exception with the given message.
	 * 
	 * @param message The message to include in the exception
	 */
	public ScoringException(String message)
	{
		super(message);
	}

	/**
	 * Creates a new scoring exception with the given message and cause.
	 * 
	 * @param message The message to include in the exception
	 * @param cause The cause of the exception
	 */
	public ScoringException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
