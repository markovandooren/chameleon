package org.aikodi.chameleon.eclipse.project;

import org.aikodi.chameleon.core.language.Language;
import org.aikodi.chameleon.eclipse.ChameleonEditorPlugin;
import org.aikodi.chameleon.eclipse.LanguageMgt;
import org.aikodi.chameleon.eclipse.builder.ChameleonBuilder;
import org.aikodi.chameleon.eclipse.connector.EclipseEditorExtension;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.workspace.BaseLibraryConfiguration;
import org.aikodi.chameleon.workspace.ConfigException;
import org.aikodi.chameleon.workspace.ProjectConfiguration;
import org.aikodi.chameleon.workspace.ProjectConfigurator;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

import java.io.File;
import java.net.MalformedURLException;

/**
 * @author Marko van Dooren
 * @author Manuel Van Wesemael 
 * @author Joeri Hendrickx 
 * 
 * A wizard class for creating new Chameleon Projects
 */
public class ProjectWizard extends BasicNewProjectResourceWizard implements INewWizard {

	public ProjectWizard() {
	}
	
	
	/**
	 * Fields for keeping track of the various pages.
	 */
	private LanguageSelectionPage _languageSelectionPage;
	
	private ProjectDetailsPage _projectPage;
	
//	private WizardNewProjectCreationPage _projectDetailsPage;
	
	private PathPage _pathPage;
	
	@Override
	public void setContainer(IWizardContainer wizardContainer) {
		super.setContainer(wizardContainer);
		//FIXME stupid, but I'm tired of Eclipse developers making
		//so many things impossible by hiding everything behind
		//XML. So far this is the "best" way I found to attach
		// a project changed listener
		if(wizardContainer instanceof WizardDialog) {
			_pathPage.notifyWizardDialogConnected((WizardDialog)wizardContainer);
		}
	}
	
	@Override
   public void init(IWorkbench workbench, IStructuredSelection selection) {
		IWizardContainer container = getContainer();
		// Why can't Eclipse allow me to query the $%*&#@$ objects.
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

	/**
	 * Initialize the pages of this wizard and add them to this wizard.
	 */
	@Override
   public void addPages() {
		_languageSelectionPage = new LanguageSelectionPage("Language Selection",this);
		_languageSelectionPage.setTitle("Language Selection");
		_languageSelectionPage.setDescription("Select the language to use for this project");

		_pathPage = new PathPage(this);
		_pathPage.setTitle( "Project Paths" );
		_pathPage.setDescription( "Set the source and binary paths" );
		
		_projectPage = new ProjectDetailsPage("Project Details", this);
		
		addPage(_languageSelectionPage);
		addPage(_projectPage);
		addPage(_pathPage);
	}
	
	private ProjectConfiguration _projectConfig;
	
	public ProjectConfiguration projectConfig() {
		return _projectConfig;
	}
	
	void createConfig(Language lang) {
		if(_projectConfig == null || _projectConfig.language() != lang) {
			try {
				_projectConfig = lang.plugin(ProjectConfigurator.class)
						                 .createConfigElement(_projectPage.projectName(), 
						                		                  null, 
						                		                  LanguageMgt.getInstance().workspace(),
						                		                  null, 
						                		                  new BaseLibraryConfiguration(LanguageMgt.getInstance().workspace()));
			} catch (ConfigException e) {
				throw new ChameleonProgrammerException(e);
			}
		}
	}

	public String projectName() {
		return projectConfig().getName();
	}
	
	public void setName(String name) {
		projectConfig().setName(name);
	}
	
	@Override
   public boolean performFinish() {
		try {
			_pathPage.complete();
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			IWorkspaceRoot workspaceRoot = workspace.getRoot();
			String projectName = projectName();
			IProject project = workspaceRoot.getProject(projectName);
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
