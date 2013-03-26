package be.kuleuven.cs.distrinet.chameleon.eclipse.connector;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;

import be.kuleuven.cs.distrinet.chameleon.core.language.Language;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.chameleon.input.ParseException;
import be.kuleuven.cs.distrinet.chameleon.workspace.DirectoryLoader;
import be.kuleuven.cs.distrinet.chameleon.workspace.FileInputSourceFactory;
import be.kuleuven.cs.distrinet.chameleon.workspace.ProjectException;
import be.kuleuven.cs.distrinet.chameleon.workspace.View;

/**
 * @author Joeri Hendrickx
 * @author Marko van Dooren
 * 
 * A Language Identification Interface to be implemented by all Language Models
 * The implementationclass must be called "LanguageModelID" and must be sat on the root
 * of the language-model-package
 */
public abstract class EclipseBootstrapper {

	public EclipseBootstrapper(String name,String languageVersion, String pluginID) {
		if(name == null) {
			name = "unknown language "+getClass().getPackage().getName(); 
		}
		_name = name;
		if(languageVersion == null) {
			languageVersion = "unspecified";
		}
		_languageVersion = languageVersion;
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
	public abstract Language createLanguage() throws ProjectException;
	
	protected URL pluginURL(String pluginID, String directory) throws IOException {
		return FileLocator.toFileURL(FileLocator.find(
  			Platform.getBundle(pluginID), new Path(directory), null));
	}
	
}
