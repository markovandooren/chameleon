package org.aikodi.chameleon.workspace;

import org.aikodi.chameleon.core.namespace.LazyRootNamespace;
import org.aikodi.chameleon.plugin.LanguagePluginImpl;

import java.io.File;

/**
 * A convenience super class for project configurators.
 * 
 * @author Marko van Dooren
 */
public abstract class ProjectConfiguratorImpl extends LanguagePluginImpl implements ProjectConfigurator {

	/**
	 * A template method for creating project configurations.
	 * <ol>
	 * <li>A project is created through {@link #createProject(String, File, Workspace)}.</li>
	 * <li>A view is created through {@link #createView()}.</li>
	 * <li>The view is added to the project.</li>
	 * <li>The listener is notified that the view is added.</li>
	 * <li>Any required base libraries are added through {@link #addBaseLibraries(View, BaseLibraryConfiguration)}.</li>
	 * <li>A project configuration object is created through {@link #createProjectConfig(View)}.</li>
	 * </ol>
	 * 
	 * @see org.aikodi.chameleon.workspace.ProjectConfigurator#createConfigElement(java.lang.String, java.io.File, org.aikodi.chameleon.workspace.Workspace, org.aikodi.chameleon.workspace.ProjectInitialisationListener, org.aikodi.chameleon.workspace.BaseLibraryConfiguration)
	 */
	@Override
	public ProjectConfiguration createConfigElement(String projectName, File root, Workspace workspace, ProjectInitialisationListener listener, BaseLibraryConfiguration baseLibraryConfiguration) throws ConfigException {
		Project project = createProject(projectName, root, workspace);
		View view = createView();
		project.addView(view);
		if(listener != null) {
			listener.viewAdded(view);
		}
		addBaseLibraries(view, baseLibraryConfiguration);
		return createProjectConfig(view);
	}

	/**
	 * Create a new view.
	 * @return
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @ post \result.language() == language();
   @ post \result.namespace() != null;
   @*/
	protected View createView() {
		return new View(new LazyRootNamespace(), language());
	}

	/**
	 * Create a project with the given name, root directory, and workspace.
	 * The project will be added to the workspace.
	 * 
	 * @param projectName The name of the new project.
	 * @param projectRoot The root directory of the new project.
	 * @param workspace The workspace to which the new project must be added.
	 * @return
	 */
 /*@
   @ public behavior
   @
   @ pre projectName != null;
   @ pre projectRoot != null;
   @ pre workspace != null;
   @
   @ post \result != null;
   @ post \result.name().equals(projectName);
   @ post \result.root() == projectRoot;
   @ post \result.workspace() == workspace;
   @*/
	protected Project createProject(String projectName, File projectRoot, Workspace workspace) {
		Project project = new Project(projectName, projectRoot);
		workspace.add(project);
		return project;
	}

	/**
	 * Create the actual project configuration object for the view. The view is already
	 * attached to the project, and any required base library is already added.
	 * 
	 * @param view The view in which the project files should be placed.
	 * @return
	 * @throws ConfigException
	 */
	protected abstract ProjectConfiguration createProjectConfig(View view) throws ConfigException;

	/**
	 * Add any base libraries the language may have. The default implementation does nothing.
	 * The {@link BaseLibraryConfiguration} determines of which languages the base libraries
	 * should be loaded. By default, all base libraries must be loaded. For compiling the
	 * base libraries themselves, though, the base library obviously should not be loaded.
	 * 
	 * @param view The view to which the base library should be added
	 * @param baseLibraryConfiguration Determines of which language the base libraries should be loaded.
	 */
	protected void addBaseLibraries(View view, BaseLibraryConfiguration baseLibraryConfiguration) {
	}

}