package tools.mdsd.library.standalone.initialization.impl;

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import tools.mdsd.library.standalone.initialization.InitializationTask;
import tools.mdsd.library.standalone.initialization.StandaloneInitializationException;

/**
 * Registers URIs in the EMF platform for a given project. After the registration, platform resource
 * and platform plugin URIs can properly be resolved for this project. The location of the project
 * is determined by a class of this project and a root folder name.
 */
public class ProjectURIByClasspathRegistration implements InitializationTask {

    private final Class<?> classOfProject;
    private final String projectName;
    private final String projectRootFolderName;

    public ProjectURIByClasspathRegistration(Class<?> classOfProject, String projectName,
            String projectRootFolderName) {
        this.classOfProject = classOfProject;
        this.projectName = projectName;
        this.projectRootFolderName = projectRootFolderName;
    }

    @Override
    public void initilizationWithoutPlatform() throws StandaloneInitializationException {
        var location = getProjectLocation(classOfProject, projectRootFolderName)
            .orElseThrow(() -> new StandaloneInitializationException(
                    "Could not find the file system path for project " + projectName + "."));
        new ProjectURIByLocationRegistration(location, projectName).init();
    }

    /**
     * Determine the location of a project from a given class.
     * 
     * @param clz
     *            The class to derive the project path from.
     * @param projectRootFolderName
     *            The name of the root folder to get the absolute path for.
     * @return Location of the project root.
     */
    protected static Optional<File> getProjectLocation(Class<?> clz, String projectRootFolderName) {
        var classLocation = clz.getProtectionDomain()
            .getCodeSource()
            .getLocation()
            .getPath();
        var plainLocation = URLDecoder.decode(classLocation, StandardCharsets.UTF_8);
        var projectNameIndex = plainLocation.indexOf(projectRootFolderName);
        var projectNameLength = projectRootFolderName.length();
        if (projectNameIndex < 0) {
            return Optional.empty();
        }
        var projectLocation = plainLocation.substring(0, projectNameIndex + projectNameLength);
        return Optional.of(new File(projectLocation));
    }

}
