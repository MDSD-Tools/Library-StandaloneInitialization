package tools.mdsd.library.standalone.initialization.impl;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.resource.impl.URIMappingRegistryImpl;

import tools.mdsd.library.standalone.initialization.InitializationTask;
import tools.mdsd.library.standalone.initialization.StandaloneInitializationException;

/**
 * Registers URIs in the EMF platform for a given project. After the
 * registration, platform resource and platform plugin URIs can properly be
 * resolved for this project. The location of the project is given by a {@link URI},
 * which should obviously not be just a platform URI.
 */
public class ProjectURIByURIRegistration implements InitializationTask {

	private final URI realProjectURI;
	private final String projectName;

	/**
	 * Constructs the registration task.
	 * 
	 * @param realProjectURI The {@link URI} specifying the real location of the
	 *                       project.
	 * @param projectName    The name of the project to be registered.
	 */
	public ProjectURIByURIRegistration(URI realProjectURI, String projectName) {
		this.realProjectURI = realProjectURI;
		this.projectName = projectName;
	}

	@Override
	public void initilizationWithoutPlatform() throws StandaloneInitializationException {
		EcorePlugin.getPlatformResourceMap().put(projectName, realProjectURI);
		var pluginURI = URI.createPlatformPluginURI("/" + projectName + "/", false);
		var platformURI = URI.createPlatformResourceURI("/" + projectName + "/", false);
		URIMappingRegistryImpl.INSTANCE.put(pluginURI, platformURI);
	}

}
