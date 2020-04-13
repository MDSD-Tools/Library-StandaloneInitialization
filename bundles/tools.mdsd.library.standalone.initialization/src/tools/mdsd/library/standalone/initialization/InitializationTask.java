package tools.mdsd.library.standalone.initialization;

import org.eclipse.core.runtime.Platform;

/**
 * Task to be executed during standalone initialization.
 * 
 * The task can have logic that is executed in standalone mode and when executed in an Eclipse
 * environment.
 */
@FunctionalInterface
public interface InitializationTask {

    /**
     * Indicates if the Eclipse platform is available.
     * 
     * @return True if running in Eclipse.
     */
    default boolean isPlatformRunning() {
        return Platform.isRunning();
    }

    /**
     * Performs the initialization. Depending on the availability of the Eclipse platform, specific
     * initialization code is executed.
     * 
     * @throws StandaloneInitializationException
     *             In case of an error during the initialization.
     */
    default void init() throws StandaloneInitializationException {
        if (isPlatformRunning()) {
            initializationWithPlatform();
        } else {
            initilizationWithoutPlatform();
        }
    }

    /**
     * Performs an initialization if running outside of Eclipse.
     * 
     * @throws StandaloneInitializationException
     *             In case of an error during the initialization.
     */
    void initilizationWithoutPlatform() throws StandaloneInitializationException;

    /**
     * Performs an initialization if running inside of Eclipse.
     * 
     * @throws StandaloneInitializationException
     *             In case of an error during the initialization.
     */
    default void initializationWithPlatform() throws StandaloneInitializationException {
        // usually, there is nothing to do in that case
    }

}
