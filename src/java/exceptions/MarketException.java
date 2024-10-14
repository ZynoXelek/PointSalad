package java.exceptions;

/**
 * Custom exception for market-related errors.
 */
public class MarketException extends Exception {

    /**
     * Default constructor.
     */
    public MarketException() {
        super();
    }

    /**
     * Constructor with a custom error message.
     *
     * @param message the custom error message
     */
    public MarketException(String message) {
        super(message);
    }

    /**
     * Constructor with a custom error message and a cause.
     *
     * @param message the custom error message
     * @param cause the cause of the exception
     */
    public MarketException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with a cause.
     *
     * @param cause the cause of the exception
     */
    public MarketException(Throwable cause) {
        super(cause);
    }
}
