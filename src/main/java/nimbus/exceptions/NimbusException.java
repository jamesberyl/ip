package nimbus.exceptions;

/**
 * Represents a custom exception specific to the Nimbus Chatbot.
 * This exception is thrown to indicate issues related to user input validation,
 * task processing errors, or file handling operations within the Nimbus system.
 */
public class NimbusException extends Exception {
    /**
     * Constructs a {@code NimbusException} with the specified error message.
     *
     * @param message The detailed error message describing the cause of the exception.
     */
    public NimbusException(String message) {
        super(message);
    }
}
