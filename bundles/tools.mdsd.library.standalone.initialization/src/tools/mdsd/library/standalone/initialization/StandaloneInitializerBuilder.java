package tools.mdsd.library.standalone.initialization;

import java.util.ArrayList;
import java.util.List;

import tools.mdsd.library.standalone.initialization.impl.EcoreClassPathDetection;
import tools.mdsd.library.standalone.initialization.impl.OCLEcoreRegistration;
import tools.mdsd.library.standalone.initialization.impl.ProjectURIByClasspathRegistration;
import tools.mdsd.library.standalone.initialization.impl.StandaloneInitializerImpl;

/**
 * Builder of {@link StandaloneInitializer} instances.
 */
public class StandaloneInitializerBuilder {

    private boolean ecoreClasspathDetection = true;
    private boolean ecoreOCL = true;
    private final List<InitializationTask> initializationTasks = new ArrayList<>();

    private StandaloneInitializerBuilder() {
        // intentionally left blank
    }

    /**
     * Create a new instance of a builder.
     * 
     * @return A new builder.
     */
    public static StandaloneInitializerBuilder builder() {
        return new StandaloneInitializerBuilder();
    }

    /**
     * Register meta models and other ecore extensions by parsing the classpath. The default is
     * using it.
     * 
     * @param use
     *            True for using it, false otherwise.
     * @return Modified builder instance.
     */
    public StandaloneInitializerBuilder useEcoreClasspathDetection(boolean use) {
        this.ecoreClasspathDetection = use;
        return this;
    }

    /**
     * Register the OCL Ecore implementation. The default is using it.
     * 
     * @param use
     *            True for using it, false otherwise.
     * @return Modified builder instance.
     */
    public StandaloneInitializerBuilder useEcoreOCL(boolean use) {
        this.ecoreOCL = use;
        return this;
    }

    /**
     * Register platform URIs for a project to a location determined by a class of this project.
     * 
     * This only works if the project name is equal to the name of the project root folder. If this
     * is not the case, use {@link #registerProjectURI(Class, String, String)}. Please note that the
     * path of a fragment bundle project is usually the path of the host bundle after compilation.
     * 
     * @param classOfProject
     *            A class of the project to register.
     * @param projectName
     *            The name of the project to register.
     * @return Modified builder instance.
     */
    public StandaloneInitializerBuilder registerProjectURI(Class<?> classOfProject, String projectName) {
        return registerProjectURI(classOfProject, projectName, projectName);
    }

    /**
     * Register platform URIs for a project to a location determined by a class of this project.
     * 
     * @param classOfProject
     *            A class of the project to register.
     * @param projectName
     *            The name of the project to register.
     * @param rootFolderName
     *            The name of the project's root folder.
     * @return Modified builder instance.
     */
    public StandaloneInitializerBuilder registerProjectURI(Class<?> classOfProject, String projectName,
            String rootFolderName) {
        ProjectURIByClasspathRegistration task = new ProjectURIByClasspathRegistration(classOfProject, projectName,
                rootFolderName);
        initializationTasks.add(task);
        return this;
    }

    /**
     * Add a custom initialization task to the builder.
     * 
     * @param task
     *            A custom task to execute during initialization.
     * @return Modified builder instance.
     */
    public StandaloneInitializerBuilder addCustomTask(InitializationTask task) {
        initializationTasks.add(task);
        return this;
    }

    /**
     * Builds a {@link StandaloneInitializer} based on the configuration done on the builder.
     * 
     * @return An initializer.
     */
    public StandaloneInitializer build() {
        List<InitializationTask> tasks = new ArrayList<>();
        if (ecoreClasspathDetection) {
            tasks.add(new EcoreClassPathDetection());
        }
        if (ecoreOCL) {
            tasks.add(new OCLEcoreRegistration());
        }
        tasks.addAll(initializationTasks);
        return new StandaloneInitializerImpl(tasks);
    }

}
