package tools.mdsd.library.standalone.initialization.ocl;

import org.eclipse.ocl.ecore.OCL;
import org.eclipse.ocl.ecore.delegate.OCLDelegateDomain;

import tools.mdsd.library.standalone.initialization.InitializationTask;

/**
 * Initializes the Ecore OCL implementation.
 */
public class OCLEcoreRegistration implements InitializationTask {

    @Override
    public void initilizationWithoutPlatform() {
        // OCL initialization
        OCL.initialize(null);
        OCLDelegateDomain.initialize(null);
    }

}
