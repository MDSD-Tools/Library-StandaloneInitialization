package tools.mdsd.library.standalone.initialization.impl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import tools.mdsd.library.standalone.initialization.InitializationTask;

/**
 * Performs the ecore class path initialization that discovers e.g. meta models.
 * 
 * @see <a href=
 *      "https://wiki.eclipse.org/EMF/FAQ#How_do_I_make_my_EMF_standalone_application_Eclipse-aware.3F">Eclipse
 *      wiki</a> for more details about how the discovery works
 */
public class EcoreClassPathDetection implements InitializationTask {

    @Override
    public void initilizationWithoutPlatform() {
        // Detection of Meta Models and URIs by classpath magic
        EcorePlugin.ExtensionProcessor.process(null);
    }

}
