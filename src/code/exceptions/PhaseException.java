package code.exceptions;

/**
 * Exception thrown when an error occurs during the processing of a phase.
 */
public class PhaseException extends Exception {

	/**
	 * Creates a new PhaseException.
	 */
	public PhaseException() {
		super();
	}

	/**
	 * Creates a new PhaseException with the given message.
	 * 
	 * @param message The message to include in the exception
	 */
	public PhaseException(String message) {
		super(message);
	}
	
	/**
	 * Creates a new PhaseException with the given message and cause.
	 * 
	 * @param message The message to include in the exception
	 * @param cause The cause of the exception
	 */
	public PhaseException(String message, Throwable cause) {
		super(message, cause);
	}
}
