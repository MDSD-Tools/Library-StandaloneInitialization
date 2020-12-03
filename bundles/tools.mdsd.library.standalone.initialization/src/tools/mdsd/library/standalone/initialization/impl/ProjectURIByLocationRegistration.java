package tools.mdsd.library.standalone.initialization.impl;

import java.io.File;

import org.eclipse.emf.common.util.URI;

import tools.mdsd.library.standalone.initialization.InitializationTask;

/**
 * Registers URIs in the EMF platform for a given project. After the registration, platform resource
 * and platform plugin URIs can properly be resolved for this project. The location of the project
 * has to be given.
 */
public class ProjectURIByLocationRegistration extends InitializationTaskWrapper {

    public ProjectURIByLocationRegistration(File projectRootLocation, String projectName) {
        super(createDelegate(projectRootLocation, projectName));
    }

    protected static InitializationTask createDelegate(File projectRootLocation, String projectName) {
        return new ProjectURIByURIRegistration(URI.createFileURI(projectRootLocation.getAbsolutePath()), projectName);
    }

}
