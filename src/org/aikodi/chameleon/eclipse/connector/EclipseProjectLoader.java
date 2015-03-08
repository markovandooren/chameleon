package org.aikodi.chameleon.eclipse.connector;

import org.aikodi.chameleon.plugin.LanguagePlugin;
import org.aikodi.chameleon.workspace.Project;
import org.eclipse.core.resources.IProject;

/**
 * An interface for loading non-Chameleon projects. This is useful
 * for adding analysis capabilities to existing projects.
 * 
 * @author Marko van Dooren
 */
public interface EclipseProjectLoader extends LanguagePlugin {

	/**
	 * Check whether this loader can load the given project.
	 * 
	 * @param project The project that must be loaded.
	 * @return
	 */
	public boolean canLoad(IProject project);
	
	/**
	 * Load the given project.
	 * @param project
	 * @return
	 */
 /*@
   @ public behavior
   @
   @ pre project != null;
   @ pre canLoad(project);
   @
   @ post \result != null;
   @*/
	public Project load(IProject project);

}
