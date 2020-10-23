package tools.mdsd.library.standalone.initialization.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import tools.mdsd.library.standalone.initialization.InitializationTask;
import tools.mdsd.library.standalone.initialization.StandaloneInitializationException;
import tools.mdsd.library.standalone.initialization.StandaloneInitializerBuilder;
import tools.mdsd.library.standalone.initialization.impl.ProjectURIByLocationRegistration;

/**
 * The Eclipse Project scanner allows to register multiple projects with the standalone
 * initialization which are nested inside a given folder.
 * 
 * The scanner looks for Eclipse Project files (".project") files and Jar-File Manifests
 * ("META-INF/MANIFEST.MF"). It should be added to a Standalone Initialization sequence through
 * {@link StandaloneInitializerBuilder#addCustomTask(InitializationTask)}.
 * 
 * @author Sebastian Krach
 *
 */
public class EclipseProjectScanner implements InitializationTask {

    public static final int DEFAULT_MAX_DEPTH = 3;
    public static final String PROJECT_FILE_NAME = ".project";

    private final Path basePath;

    /**
     * Creates a new Instance of the Eclipse Project Scanner Task.
     * 
     * @param basePath
     *            the path within which the scanner will look for Eclipse project files or
     *            Jar-Manifests.
     */
    public EclipseProjectScanner(Path basePath) {
        this.basePath = basePath;
    }

    @Override
    public void initilizationWithoutPlatform() throws StandaloneInitializationException {
        Map<String, File> projects = Collections.emptyMap();
        try {
            projects = findProjects(basePath);
        } catch (IOException e) {
            throw new StandaloneInitializationException("Errors reading project artifacts", e);
        }

        if (projects.isEmpty())
            throw new StandaloneInitializationException(
                    "Could not find the eclipse project artifacts in " + basePath.toString() + ".");

        for (var project : projects.entrySet()) {
            (new ProjectURIByLocationRegistration(project.getValue(), project.getKey())).init();
        }
    }

    protected static Map<String, File> findProjects(Path basePath) throws IOException {
        var projectFilePath = Paths.get(PROJECT_FILE_NAME);

        var res = Files
            .find(basePath, DEFAULT_MAX_DEPTH + projectFilePath.getNameCount(),
                    (path, attr) -> path.endsWith(projectFilePath))
            .collect(Collectors.toMap(EclipseProjectScanner::readProjectNameFromProjectFile, path -> {
                var result = path;
                // Using subpath removes the root, therefore we loop manually
                for (int i = 0; i < projectFilePath.getNameCount(); i++) {
                    result = result.getParent();
                }
                return result.toFile();
            }));

        var manifestPath = Paths.get(JarFile.MANIFEST_NAME);
        res.putAll(Files
            .find(basePath, DEFAULT_MAX_DEPTH + manifestPath.getNameCount(),
                    (path, attr) -> path.endsWith(Paths.get(JarFile.MANIFEST_NAME)))
            .collect(Collectors.toMap(EclipseProjectScanner::readProjectNameFromManifestFile, path -> {
                var result = path;
                // Using subpath removes the root, therefore we loop manually
                for (int i = 0; i < manifestPath.getNameCount(); i++) {
                    result = result.getParent();
                }
                return result.toFile();
            }, (a, b) -> b)));

        return res;
    }

    protected static String readProjectNameFromProjectFile(Path path) {
        Document document;
        try {
            document = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(Files.newInputStream(path, StandardOpenOption.READ));
            return document.getDocumentElement()
                .getElementsByTagName("name")
                .item(0)
                .getTextContent();
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new RuntimeException("Error reading the project file " + path.toString(), e);
        }
    }

    protected static String readProjectNameFromManifestFile(Path path) {
        try {
            Manifest manifest = new Manifest(Files.newInputStream(path, StandardOpenOption.READ));
            var bundleName = manifest.getMainAttributes()
                .getValue("Bundle-SymbolicName");
            if (bundleName.indexOf(';') > 0) {
                bundleName = bundleName.substring(0, bundleName.indexOf(';'));
            }
            return bundleName;
        } catch (IOException e) {
            throw new RuntimeException("Error reading the project file " + path.toString(), e);
        }
    }

}
