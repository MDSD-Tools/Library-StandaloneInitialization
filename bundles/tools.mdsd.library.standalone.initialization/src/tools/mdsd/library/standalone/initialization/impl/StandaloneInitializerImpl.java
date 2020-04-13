package tools.mdsd.library.standalone.initialization.impl;

import java.util.ArrayList;
import java.util.List;

import tools.mdsd.library.standalone.initialization.InitializationTask;
import tools.mdsd.library.standalone.initialization.StandaloneInitializationException;
import tools.mdsd.library.standalone.initialization.StandaloneInitializer;

/**
 * Implementation of an {@link StandaloneInitializer} that executes all registered
 * {@link InitializationTask} elements in sequence.
 */
public class StandaloneInitializerImpl implements StandaloneInitializer {

    private final List<InitializationTask> tasks = new ArrayList<>();

    /**
     * Constructs the initializer.
     * 
     * @param tasks
     *            All tasks to be executed in the given sequence during initialization.
     */
    public StandaloneInitializerImpl(List<InitializationTask> tasks) {
        this.tasks.addAll(tasks);
    }

    @Override
    public void init() throws StandaloneInitializationException {
        for (InitializationTask task : tasks) {
            task.init();
        }
    }

}
