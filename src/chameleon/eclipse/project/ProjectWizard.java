package chameleon.eclipse.project;

import java.io.File;
import java.net.MalformedURLException;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

import chameleon.core.language.Language;
import chameleon.eclipse.ChameleonEditorPlugin;
import chameleon.eclipse.builder.ChameleonBuilder;
import chameleon.eclipse.connector.EclipseEditorExtension;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.workspace.ConfigException;
import chameleon.workspace.ProjectConfig;
import chameleon.workspace.ProjectConfigurator;

/**
 * @author Marko van Dooren
 * @author Manuel Van Wesemael 
 * @author Joeri Hendrickx 
 * 
 * A wizard class for creating new Chameleon Projects
 */
public class ProjectWizard extends BasicNewProjectResourceWizard implements INewWizard {

	/**
	 * Fields for keeping track of the various pages.
	 */
	private LanguageSelectionPage _languageSelectionPage;
	
	private ProjectDetailsPage _projectPage;
	
//	private WizardNewProjectCreationPage _projectDetailsPage;
	
	private PathPage _pathPage;
	
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		setWindowTitle("Create New Chameleon Project");
		try {
			setDefaultPageImageDescriptor(EclipseEditorExtension.iconDescriptor("chameleon.png", ChameleonEditorPlugin.PLUGIN_ID));
		} catch (MalformedURLException e) {
		}
	}
	
	public IPath projectRootPath() {
		return Path.fromOSString(_projectPage.projectPath());
	}
	
	public IPath workspacePath() {
		return ResourcesPlugin.getWorkspace().getRoot().getLocation();
	}
	
	void setRoot(String root) {
		File rootFile = new File(root);
		projectConfig().setRoot(rootFile);
	}

//	public IPath projectRoot() {
//		return project().getLocation();
//	}
//	
//	public IProject project() {
//		IProject projectHandle = _projectDetailsPage.getProjectHandle();
//		Object x = projectHandle.getLocationURI();
//		if(! projectHandle.exists()) {
////			try {
////				projectHandle.create(new NullProgressMonitor());
////				projectHandle.close(new NullProgressMonitor());
////			} catch (CoreException e) {
////			}
//		}
//		return projectHandle;
//	}

	/**
	 * Initialize the pages of this wizard and add them to this wizard.
	 */
	public void addPages() {
		_languageSelectionPage = new LanguageSelectionPage("Language Selection",this);
		_languageSelectionPage.setTitle("Language Selection");
		_languageSelectionPage.setDescription("Select the language to use for this project");

//		_projectDetailsPage = new WizardNewProjectCreationPage("Project Details");
//		_projectDetailsPage.setTitle( "Project Details" );
//		_projectDetailsPage.setDescription( "Fill in a project title" );

		_pathPage = new PathPage(this);
		_pathPage.setTitle( "Project Paths" );
		_pathPage.setDescription( "Set the source and binary paths" );
		
		_projectPage = new ProjectDetailsPage("Project Details", this);
		
		addPage(_languageSelectionPage);
		addPage(_projectPage);
		addPage(_pathPage);
	}
	
	private ProjectConfig _projectConfig;
	
	public ProjectConfig projectConfig() {
		return _projectConfig;
	}
	
	void createConfig(Language lang) {
		if(_projectConfig == null || _projectConfig.language() != lang) {
			try {
				_projectConfig = lang.plugin(ProjectConfigurator.class).createConfigElement(_projectPage.projectName(), null, null);
			} catch (ConfigException e) {
				throw new ChameleonProgrammerException(e);
			}
		}
	}

	public String projectName() {
		return projectConfig().name();
	}
	
	public void setName(String name) {
		projectConfig().setName(name);
	}
	
	public boolean performFinish() {
		try {
			_pathPage.complete();
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			IWorkspaceRoot workspaceRoot = workspace.getRoot();
			String projectName = projectName();
			IProject project = workspaceRoot.getProject(projectName);
//			IFile projfile = project.getFile("."+ChameleonProjectNature.CHAMELEON_PROJECT_FILE_EXTENSION);
//			IFile xmlFile = project.getFile(ChameleonProjectNature.CHAMELEON_PROJECT_FILE);
			File xmlFile = new File(projectDirectory().getAbsolutePath()+File.separator+ChameleonProjectNature.CHAMELEON_PROJECT_FILE);
			try {
				projectConfig().writeToXML(xmlFile);
			} catch (Throwable e) {
				e.printStackTrace();
			}
			if (!project.exists()) {
				IProjectDescription newProjectDescription = workspace.newProjectDescription(projectName);
//				newProjectDescription.setLocation(projectRootPath());
				project.create(newProjectDescription,new NullProgressMonitor());
			}

			if(!project.isOpen()) {
				project.open(new NullProgressMonitor());
			}
			
//			String name = projectLanguage().name();
//			InputStream in = new StringBufferInputStream(name);
//			projfile.create(in,true,null);

			IProjectDescription description = project.getDescription();
			String[] natures = description.getNatureIds();
			String[] newNatures = new String[natures.length + 1];
			System.arraycopy(natures, 0, newNatures, 0, natures.length);
			newNatures[natures.length] = ChameleonProjectNature.NATURE;
			description.setNatureIds(newNatures);
			ICommand[] builders = new ICommand[1];
			ICommand command = description.newCommand();
			command.setBuilderName(ChameleonBuilder.BUILDER_ID);
			builders[0] = command;
			description.setBuildSpec(builders);
			project.setDescription(description, null);

			ChameleonProjectNature chameleonNature = ((ChameleonProjectNature)project.getNature(ChameleonProjectNature.NATURE));
			chameleonNature.setProject(project);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public File projectDirectory() {
		return _projectPage.projectDirectory();
	}

	protected Language projectLanguage() {
		return _languageSelectionPage.selectedLanguage();
	}
}