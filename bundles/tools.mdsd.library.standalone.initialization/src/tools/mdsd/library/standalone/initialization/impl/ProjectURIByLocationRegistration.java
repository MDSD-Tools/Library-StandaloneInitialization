package tools.mdsd.library.standalone.initialization.impl;

import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.resource.impl.URIMappingRegistryImpl;

import tools.mdsd.library.standalone.initialization.InitializationTask;
import tools.mdsd.library.standalone.initialization.StandaloneInitializationException;

/**
 * Registers URIs in the EMF platform for a given project. After the registration, platform resource
 * and platform plugin URIs can properly be resolved for this project. The location of the project
 * has to be given.
 */
public class ProjectURIByLocationRegistration implements InitializationTask {

    private final File projectRootLocation;
    private final String projectName;

    public ProjectURIByLocationRegistration(File projectRootLocation, String projectName) {
        this.projectRootLocation = projectRootLocation;
        this.projectName = projectName;
    }

    @Override
    public void initilizationWithoutPlatform() throws StandaloneInitializationException {
        var projectURI = URI.createFileURI(projectRootLocation.getAbsolutePath())
            .appendSegment("");
        EcorePlugin.getPlatformResourceMap()
            .put(projectName, projectURI);
        var pluginURI = URI.createPlatformPluginURI("/" + projectName + "/", false);
        var platformURI = URI.createPlatformResourceURI("/" + projectName + "/", false);
        URIMappingRegistryImpl.INSTANCE.put(pluginURI, platformURI);
    }

}
