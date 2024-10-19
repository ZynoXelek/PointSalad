package main.java.exceptions;

/**
 * Custom exception for client-related errors.
 */
public class ServerException extends Exception {
	
	/**
	 * Creates a new server exception.
	 */
	public ServerException()
	{
		super();
	}

	/**
	 * Creates a new server exception with the given message.
	 * 
	 * @param message The message to include in the exception
	 */
	public ServerException(String message)
	{
		super(message);
	}

	/**
	 * Creates a new server exception with the given message and cause.
	 * 
	 * @param message The message to include in the exception
	 * @param cause The cause of the exception
	 */
	public ServerException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
}
