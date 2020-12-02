package tools.mdsd.library.standalone.initialization.impl;

import java.io.File;

import org.eclipse.emf.common.util.URI;

import tools.mdsd.library.standalone.initialization.InitializationTask;
import tools.mdsd.library.standalone.initialization.StandaloneInitializationException;

/**
 * Registers URIs in the EMF platform for a given project. After the
 * registration, platform resource and platform plugin URIs can properly be
 * resolved for this project. The location of the project has to be given.
 */
public class ProjectURIByLocationRegistration implements InitializationTask {

	private final InitializationTask delegate;

	public ProjectURIByLocationRegistration(File projectRootLocation, String projectName) {
		this.delegate = new ProjectURIByURIRegistration(URI.createFileURI(projectRootLocation.getAbsolutePath()),
				projectName);
	}

	@Override
	public void initilizationWithoutPlatform() throws StandaloneInitializationException {
		delegate.initilizationWithoutPlatform();
	}

	@Override
	public void initializationWithPlatform() throws StandaloneInitializationException {
		delegate.initializationWithPlatform();
	}

}
