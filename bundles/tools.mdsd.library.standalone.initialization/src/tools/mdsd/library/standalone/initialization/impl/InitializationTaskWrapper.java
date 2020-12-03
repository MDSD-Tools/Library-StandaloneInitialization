package tools.mdsd.library.standalone.initialization.impl;

import tools.mdsd.library.standalone.initialization.InitializationTask;
import tools.mdsd.library.standalone.initialization.StandaloneInitializationException;

/**
 * Wrapper for an {@link InitializationTask} that delegates all publicly available calls to the
 * wrapped task.
 */
public class InitializationTaskWrapper implements InitializationTask {

    private final InitializationTask delegate;

    /**
     * Constructs the wrapper with the task to delegate to.
     * 
     * @param delegate
     *            The task to delegate to.
     */
    public InitializationTaskWrapper(InitializationTask delegate) {
        this.delegate = delegate;
    }

    @Override
    public void initilizationWithoutPlatform() throws StandaloneInitializationException {
        getDelegate().initilizationWithoutPlatform();
    }

    @Override
    public void initializationWithPlatform() throws StandaloneInitializationException {
        getDelegate().initializationWithPlatform();
    }

    /**
     * Tries to find and return the {@link InitializationTask} to which we want to delegate.
     * 
     * @return The delegation target.
     * @throws StandaloneInitializationException
     *             if the delegate could not be found.
     */
    protected InitializationTask getDelegate() throws StandaloneInitializationException {
        return delegate;
    }

}
