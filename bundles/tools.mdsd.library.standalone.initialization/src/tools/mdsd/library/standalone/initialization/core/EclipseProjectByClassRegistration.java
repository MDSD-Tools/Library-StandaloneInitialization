package tools.mdsd.library.standalone.initialization.core;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;

import tools.mdsd.library.standalone.initialization.InitializationTask;
import tools.mdsd.library.standalone.initialization.StandaloneInitializationException;
import tools.mdsd.library.standalone.initialization.StandaloneInitializerBuilder;
import tools.mdsd.library.standalone.initialization.impl.ProjectURIByLocationRegistration;

/**
 * This Initialization Task allows to register the eclipse project which encloses a given class.
 * 
 * The task looks for Eclipse Project files (".project") files and Jar-File Manifests
 * ("META-INF/MANIFEST.MF"). It should be added to a Standalone Initialization sequence through
 * {@link StandaloneInitializerBuilder#addCustomTask(InitializationTask)}.
 * 
 * @author Sebastian Krach
 *
 */
public class EclipseProjectByClassRegistration implements InitializationTask {
    private Class<?> clazz;

    /**
     * Creates a new instance of the initialization task.
     * 
     * @param clazz
     *            the class which is used to look up the enclosing project. Therefore, it needs to
     *            be located on the file system.
     */
    public EclipseProjectByClassRegistration(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void initilizationWithoutPlatform() throws StandaloneInitializationException {
        for (var project : tryFindProjectContaining(clazz).entrySet()) {
            (new ProjectURIByLocationRegistration(project.getValue(), project.getKey())).init();
        }
    }

    protected static Map<String, File> tryFindProjectContaining(Class<?> clz) throws StandaloneInitializationException {
        Map<String, File> result = Collections.emptyMap();
        try {
            var classLocation = Paths.get(clz.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI());
            while (classLocation != null && result.isEmpty()) {
                result = EclipseProjectScanner.findProjects(classLocation);
                classLocation = classLocation.getParent();
            }
        } catch (URISyntaxException | IOException e) {
            throw new StandaloneInitializationException("Error locating the class in the file system", e);
        }

        if (result.isEmpty()) {
            throw new StandaloneInitializationException(
                    "Error locating a eclipse project artifact in the file system hierarchy");
        }
        return result;
    }
}
