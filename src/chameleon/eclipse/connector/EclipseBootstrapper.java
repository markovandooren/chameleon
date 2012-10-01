package chameleon.eclipse.connector;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;

import chameleon.core.document.Document;
import chameleon.core.language.Language;
import chameleon.eclipse.LanguageMgt;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.exception.ModelException;
import chameleon.input.ModelFactory;
import chameleon.input.ParseException;
import chameleon.plugin.Plugin;
import chameleon.plugin.PluginImpl;
import chameleon.plugin.build.BuildProgressHelper;
import chameleon.plugin.build.Builder;
import chameleon.workspace.DirectoryLoader;
import chameleon.workspace.FileInputSourceFactory;
import chameleon.workspace.Project;
import chameleon.workspace.ProjectException;

/**
 * @author Joeri Hendrickx
 * @author Marko van Dooren
 * 
 * A Language Identification Interface to be implemented by all Language Models
 * The implementationclass must be called "LanguageModelID" and must be sat on the root
 * of the language-model-package
 */
public abstract class EclipseBootstrapper {

	private EclipseBootstrapper() {
		_extensions = new ArrayList<String>();
		registerFileExtensions();
	}
	
	public EclipseBootstrapper(String name,String languageVersion, String extension, String pluginID) {
		this();
		if(name == null) {
			name = "unknown language "+getClass().getPackage().getName(); 
		}
		_name = name;
		if(languageVersion == null) {
			languageVersion = "unspecified";
		}
		_languageVersion = languageVersion;
		addExtension(extension);
	}
	
	public String pluginID() {
		return _pluginID;
	}
	
	private String _pluginID;
	
	private String _name;
	
	/**
	 * @return Informal name of the supported language
	 */
	public String getLanguageName() {
		return _name;
	}
	
	/**
	 * @return Version information of the supported language
	 */
	public String getLanguageVersion() {
		return _languageVersion;
	}
	
	private String _languageVersion;


	public List<String> fileExtensions() {
		return new ArrayList<String>(_extensions);
	}
	
	protected void addExtension(String extension) {
		_extensions.add(extension);
	}
	
	protected void removeExtension(String extension) {
		_extensions.remove(extension);
	}
	
	private List<String> _extensions;
	
	/**
	 * Does nothing by default. If a single extension is used, it can be provided
	 * via the constructor. If multiple extensions are used, overrides this method.
	 */
	public void registerFileExtensions() {
		
	}

	/**
	 * Create the language object, and attach at least the following extensions:
	 * <ul>
	 *   <li>chameleon.output.Syntax</li>
	 *   <li>chameleon.eclipse.connector.EclipseEditorExtension</li>
	 * </ul>
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 * @throws ProjectException 
	 */
	public abstract Language createLanguage() throws IOException, ParseException, ProjectException;
	
	protected URL pluginURL(String pluginID, String directory) throws IOException {
		return FileLocator.toFileURL(FileLocator.find(
  			Platform.getBundle(pluginID), new Path(directory), null));
	}
	
	protected void loadAPIFiles(String extension, String pluginId, Project project,FileInputSourceFactory factory) throws IOException, ParseException, ProjectException {
		URL directory;
		try {
		  directory = pluginURL(pluginId, "api/");
		} catch(NullPointerException exc) {
			throw new ChameleonProgrammerException("No directory named 'api' is found to load the API.");
		}
		File root = new File(directory.getFile());
		project.addSource(new DirectoryLoader(extension, root, factory));
		// FIXME: This should be done by a reusable artefact.
		project.language().plugin(ModelFactory.class).initializePredefinedElements();
	}

}
