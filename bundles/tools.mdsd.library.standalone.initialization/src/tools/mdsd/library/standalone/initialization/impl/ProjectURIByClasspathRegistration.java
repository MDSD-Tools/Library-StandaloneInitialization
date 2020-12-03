package tools.mdsd.library.standalone.initialization.impl;

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;

import tools.mdsd.library.standalone.initialization.InitializationTask;

/**
 * Registers URIs in the EMF platform for a given project. After the registration, platform resource
 * and platform plugin URIs can properly be resolved for this project. The location of the project
 * is determined by a class of this project and a root folder name.
 */
public class ProjectURIByClasspathRegistration extends InitializationTaskWrapper {

    /**
     * Constructs the project registration task.
     * 
     * @param classOfProject
     *            A class within the project to be registered.
     * @param projectName
     *            The name of the project to be registered.
     * @param projectRootFolderName
     *            The name of the project's root folder (most probably same as project name).
     */
    public ProjectURIByClasspathRegistration(Class<?> classOfProject, String projectName,
            String projectRootFolderName) {
        super(createDelegate(classOfProject, projectName, projectRootFolderName));
    }

    /**
     * Creates the delegate to be called, which is also a registration task for projects.
     * 
     * @param classOfProject
     *            A class within the project to be registered.
     * @param projectName
     *            The name of the project to be registered.
     * @param projectRootFolderName
     *            The name of the project's root folder (most probably same as project name).
     * @return The delegate to be used.
     */
    protected static InitializationTask createDelegate(Class<?> classOfProject, String projectName,
            String projectRootFolderName) {
        var projectUri = getRealProjectURI(classOfProject, projectRootFolderName);
        return new ProjectURIByURIRegistration(projectUri.orElse(null), projectName);
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
    protected static Optional<URI> getRealProjectURI(Class<?> clz, String projectRootFolderName) {
        var classLocation = clz.getProtectionDomain()
            .getCodeSource()
            .getLocation()
            .getPath();
        var plainLocation = URLDecoder.decode(classLocation, StandardCharsets.UTF_8);
        if (plainLocation.endsWith(".jar")) {
            var plainFile = new File(plainLocation);
            var fileURI = URI.createFileURI(plainFile.getAbsolutePath());
            var authority = fileURI.toString() + "!";
            var jarUri = URI.createHierarchicalURI("jar", authority, null, new String[] { "" }, null, null);
            return Optional.of(jarUri);
        }
        var projectNameIndex = plainLocation.indexOf(projectRootFolderName);
        var projectNameLength = projectRootFolderName.length();
        if (projectNameIndex < 0) {
            return Optional.empty();
        }
        var projectLocation = plainLocation.substring(0, projectNameIndex + projectNameLength);
        var projectLocationFile = new File(projectLocation);
        var fileUri = URI.createFileURI(projectLocationFile.getAbsolutePath())
            .appendSegment("");
        return Optional.of(fileUri);
    }

}
