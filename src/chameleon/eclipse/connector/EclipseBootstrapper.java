package chameleon.eclipse.connector;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import chameleon.core.language.Language;
import chameleon.eclipse.LanguageMgt;
import chameleon.input.ModelFactory;
import chameleon.input.ParseException;
import chameleon.plugin.build.Builder;
import chameleon.plugin.output.Syntax;

/**
 * @author Joeri Hendrickx
 * @author Marko van Dooren
 * 
 * A Language Identification Interface to be implemented by all Language Models
 * The implementationclass must be called "LanguageModelID" and must be sat on the root
 * of the language-model-package
 */
public abstract class EclipseBootstrapper {

	public EclipseBootstrapper() {
		_extensions = new ArrayList<String>();
		registerFileExtensions();
	}
	
	/**
	 * @return Informal name of the supported language
	 */
	public abstract String getLanguageName();
	
	/**
	 * @return Version information of the supported language
	 */
	public abstract String getLanguageVersion();
	
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
	
	public abstract void registerFileExtensions();

	
//	/**
//	 * @return Version information of the Language Model
//	 */
//	String getVersion();
	
	/**
	 * @return General description of this language model
	 */
	public abstract String getDescription();
	
	/**
	 * @return Licensing information for the language model
	 */
	public abstract String getLicense();
	
	/**
	 * Create the language object, and attach at least the following extensions:
	 * <ul>
	 *   <li>chameleon.output.Syntax</li>
	 *   <li>chameleon.eclipse.connector.EclipseEditorExtension</li>
	 * </ul>
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public abstract Language createLanguage() throws IOException, ParseException;
	
	/**
	 * @return the CodeWriter for this language
	 */
	public abstract Syntax getCodeWriter();
	
	// FIXME: must be a language/project extension (or move to editor extension)
	/**
	 * The resulting builder must be connected to a fresh language object. A syntax connector
	 * must be attached to that language. The builder must be connected to that language.
	 */
	public Builder createBuilder(Language source, File projectDirectory) {
		return null;
	}
	
	protected void loadAPIFiles(String extension, String pluginId, ModelFactory factory) throws IOException, ParseException {
		FilenameFilter filter = LanguageMgt.fileNameFilter(extension);
		URL directory = LanguageMgt.pluginURL(pluginId, "api/");
		List<File> files = LanguageMgt.allFiles(directory, filter);
		System.out.println("Loading "+files.size()+" API files.");	
		factory.initializeBase(files);
	}

}
