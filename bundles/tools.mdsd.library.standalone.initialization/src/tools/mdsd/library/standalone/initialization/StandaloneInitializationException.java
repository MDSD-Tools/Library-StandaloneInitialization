package tools.mdsd.library.standalone.initialization;

/**
 * Exception to be throw in case of an error during the initialization.
 */
public class StandaloneInitializationException extends Exception {

    private static final long serialVersionUID = 2033299280210882248L;

    /**
     * See {@link Exception#Exception(String,Throwable)}
     * 
     * @param message
     *            The message to be recorded.
     * @param cause
     *            The cause to be recorded.
     */
    public StandaloneInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * See {@link Exception#Exception(String)}
     * 
     * @param message
     *            The message to be recorded.
     */
    public StandaloneInitializationException(String message) {
        super(message);
    }

}
