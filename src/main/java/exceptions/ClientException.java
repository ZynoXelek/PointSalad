package exceptions;

/**
 * Custom exception for client-related errors.
 */
public class ClientException extends Exception {
	
	/**
	 * Creates a new client exception.
	 */
	public ClientException()
	{
		super();
	}

	/**
	 * Creates a new client exception with the given message.
	 * 
	 * @param message The message to include in the exception
	 */
	public ClientException(String message)
	{
		super(message);
	}

	/**
	 * Creates a new client exception with the given message and cause.
	 * 
	 * @param message The message to include in the exception
	 * @param cause The cause of the exception
	 */
	public ClientException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
}
