package tools.mdsd.library.standalone.initialization.emfprofiles;

import org.modelversioning.emfprofile.Profile;
import org.modelversioning.emfprofile.registry.IProfileRegistry;

import tools.mdsd.library.standalone.initialization.InitializationTask;
import tools.mdsd.library.standalone.initialization.StandaloneInitializationException;
import tools.mdsd.library.standalone.initialization.StandaloneInitializerBuilder;
import tools.mdsd.library.standalone.initialization.core.MetaModelRegistrationTask;

/**
 * Initialization task for a {@link Profile} of EMF Profiles.
 * 
 * This task is not self-contained. In order to make the registration work, callers have to ensure
 * that the project containing the profile is already known to the EMF registry by using
 * initialization tasks like {@link StandaloneInitializerBuilder#registerProjectURI(Class, String)}.
 */
public class EMFProfileInitializationTask implements InitializationTask {

    private final InitializationTask profileRegistrationTask;

    /**
     * Constructs the task.
     * 
     * @param projectName
     *            The name of the project that holds the profile.
     * @param profilePath
     *            The path to the profile model relative to the project without leading slash.
     */
    public EMFProfileInitializationTask(String projectName, String profilePath) {
        profileRegistrationTask = new MetaModelRegistrationTask(projectName, profilePath);
    }

    @Override
    public void initilizationWithoutPlatform() throws StandaloneInitializationException {
        profileRegistrationTask.initilizationWithoutPlatform();
    }

    @Override
    public void initializationWithPlatform() throws StandaloneInitializationException {
        /*
         * The profile registry reads extension points and registers profiles. The registry is a
         * singleton, so executing this multiple times does not do any harm. Assuming the profile
         * bundle is on the classpath and properly registers the profile via extension point, it is
         * automatically registered.
         */
        IProfileRegistry.eINSTANCE.getClass();
    }

}