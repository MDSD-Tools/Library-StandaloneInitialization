package tools.mdsd.library.standalone.initialization.impl;

import java.util.LinkedList;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

import tools.mdsd.library.standalone.initialization.InitializationTask;
import tools.mdsd.library.standalone.initialization.StandaloneInitializationException;

/**
 * Initilization task for manually registering EMF meta models.
 * 
 * This implementation assumes that the project containing the meta model has already been registered
 * with the EMF registries. Callers have to ensure the proper state of the registries, e.g. by
 * registering the project with another {@link InitializationTask}.
 */
public class MetaModelRegistrationTask implements InitializationTask {

    private final String projectName;
    private final String metaModelPath;

    /**
     * Constructs the task
     * 
     * @param projectName
     *            The name of the project hosting the profile.
     * @param metaModelPath
     *            The path of the meta model relative to the given project without leading slash.
     */
    public MetaModelRegistrationTask(String projectName, String metaModelPath) {
        this.projectName = projectName;
        this.metaModelPath = metaModelPath;
    }

    @Override
    public void initilizationWithoutPlatform() throws StandaloneInitializationException {
        var rs = new ResourceSetImpl();
        var uri = URI.createPlatformPluginURI(String.format("/%s/%s", projectName, metaModelPath), false);
        
        var queue = new LinkedList<EPackage>();
        try {
            var epackage = (EPackage) rs.getResource(uri, true)
                .getContents()
                .get(0);
            queue.add(epackage);
        } catch (WrappedException e) {
            throw new StandaloneInitializationException("Could not load profile. Please check preconditions.",
                    e.getCause());
        }
        
        while(!queue.isEmpty()) {
            var epackage = queue.pop();
            EPackageRegistryImpl.INSTANCE.put(epackage.getNsURI(), epackage);
            queue.addAll(epackage.getESubpackages());            
        }
    }

}
