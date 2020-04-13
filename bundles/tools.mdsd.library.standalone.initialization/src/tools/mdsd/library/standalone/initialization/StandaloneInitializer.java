package tools.mdsd.library.standalone.initialization;

/**
 * Initializer that carries out standalone initialization when called.
 * 
 * Use {@link StandaloneInitializerBuilder} to get an instance.
 */
public interface StandaloneInitializer {

    /**
     * Performs the initialization task.
     * 
     * @throws StandaloneInitializationException
     *             In case of an error during initialization.
     */
    void init() throws StandaloneInitializationException;

}
