package org.aikodi.chameleon.eclipse.connector;

import java.io.IOException;
import java.net.URL;

import org.aikodi.chameleon.core.language.Language;
import org.aikodi.chameleon.input.ParseException;
import org.aikodi.chameleon.workspace.ProjectException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;

/**
 * A boot strapper class to load a language module into Eclipse.
 * 
 * @author Marko van Dooren
 */
public abstract class EclipseBootstrapper {

  /**
   * 
   * @param name
   * @param languageVersion
   * @param pluginID The identifier of the plugin. This must be exactly the same as the identifier
   *                 in the plugin.xml file.
   */
	public EclipseBootstrapper() {
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
	public abstract Language createLanguage() throws ProjectException;
	
	protected URL pluginURL(String pluginID, String directory) throws IOException {
		return FileLocator.toFileURL(FileLocator.find(
  			Platform.getBundle(pluginID), new Path(directory), null));
	}
	
}
