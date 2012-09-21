package chameleon.eclipse.project;

import java.io.InputStream;
import java.io.StringBufferInputStream;

import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

import chameleon.eclipse.LanguageMgt;
import chameleon.eclipse.builder.ChameleonBuilder;

/**
 * @author Manuel Van Wesemael 
 * @author Joeri Hendrickx 
 * 
 * A wizard class for creating new Chameleon Projects
 */
public class ProjectWizard extends BasicNewProjectResourceWizard implements INewWizard {

	private IWizardPage[] pages;


	
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		setWindowTitle("New Chameleon Project");
		setDefaultPageImageDescriptor(ImageDescriptor.createFromFile(getClass(),"../../../icons/chameleon32.png"));
	}

    public void addPages()
    {
		pages = new IWizardPage[2];
		pages[0] = new LanguageSectionPage("Language Selection");
		pages[1] = new ProjectDetailsPage("Project Details");
		pages[0].setTitle("Language Selection");
		pages[0].setDescription("Select the language to use for this project");

		pages[1].setTitle( "Project Details" );
		pages[1].setDescription( "Fill in a project title" );

        addPage(pages[0]);
        addPage(pages[1]);
        	
    }


	class LanguageSectionPage extends WizardPage {


		private void createPageContent(){
			
			optionList = new List(controlContainer, SWT.NONE);
			optionList.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			
			String[] lang = LanguageMgt.getInstance().getLanguageStrings();
			for (int i = 0; i < lang.length; i++) {
				optionList.add(lang[i]);
			}
			optionList.setVisible(true);
			
		}
		
		protected LanguageSectionPage(String pageName) {
			super(pageName);
			
		}

		private int selected;
		private Composite controlContainer;
		private List optionList;
		
		
		public boolean canFlipToNextPage() {
			return isPageComplete();
		}
		
		public boolean isPageComplete(){
			System.out.println("checking");
			return optionList.getSelection().length>0;
		}

		public String getName() {
			return "Select the Language this project should use";
		}

		public void createControl(Composite parent) {
			controlContainer = new Composite(parent,SWT.NONE);
			controlContainer.setLayout( new FillLayout(  ) );
			controlContainer.setLayoutData( new GridData( GridData.FILL_BOTH ) );
	        createPageContent( );
			GridLayout gl = new GridLayout();
		    int ncol = 20;
		    gl.numColumns = ncol;
		    controlContainer.setLayout(gl);	
	        setControl( controlContainer );
	        setPageComplete( true );
	        optionList.addSelectionListener(new SelectionListener(){
				public void widgetSelected(SelectionEvent e) {
					getWizard().getContainer().updateButtons();
				}
				public void widgetDefaultSelected(SelectionEvent e) {
					getWizard().getContainer().updateButtons();
				}});

			
			
		}





		public String getDescription() {
			return "Allows the user to select the language to use in this Chameleonproject";
		}

		public String getErrorMessage() {
			if (selected<0) return "No Language selected";
			return null; 
		}



		public String getMessage() {
			return "Please select the language to use for this project";
		}

		public String getTitle() {
			return "Language Selection";
		}


		public void setVisible(boolean visible) {
			controlContainer.setVisible(visible);
			
		}		
		
		public String getProjectLanguage(){
			return optionList.getSelection()[0];
			
		}
		
	}

	class ProjectDetailsPage extends WizardPage implements IWizardPage {


		protected ProjectDetailsPage(String pageName) {
			super(pageName);
			
		}
		


		public Text projectTitle;
		private Composite controlContainer;
		
		

		public void createControl(Composite parent) {
			controlContainer = new Composite(parent,SWT.NONE);
			GridLayout gl = new GridLayout();
		    //int ncol = 10;
		    controlContainer.setLayout(gl);
			projectTitle = new Text(controlContainer,SWT.BORDER);
			projectTitle.setBounds(0,55,300,15);
			projectTitle.setLayoutData(new GridData(55,15));
			
			projectTitle.setText("MyProject");
			
	
			setControl(controlContainer);
		}

		public void setVisible(boolean visible) {
			controlContainer.setVisible(visible);
			super.setVisible(visible);
		}

		public String getProjectName() {
			return projectTitle.getText();
		}		
		
	}
	
	/**
	 * @see IWorkspaceRoot#getProject(String)
	 */


	public boolean performFinish() {
		//project structuur aanmaken
		//van het type chameleonproject
		//taal moet gezet worden ergens => modelfactory
		try {
			Workspace workspace =(Workspace) ResourcesPlugin.getWorkspace();
			IWorkspaceRoot workspaceRoot = workspace.getRoot();
			//volgende geeft het probleem met de classcastexception in chameleondocumentprovider
			//workspace zet nl een project in zijn table, en niet een chameleonproject
			IProject project = workspaceRoot.getProject(((ProjectDetailsPage)pages[1]).getProjectName());
			IFile projfile = project.getFile("."+ChameleonProjectNature.CHAMELEON_PROJECT_FILE_EXTENSION);

			if (!project.exists()) {
					//dit doet intern ook project.create() wat een project creert in de workspace
					//project = new ChameleonProject(project.getFullPath(),workspace,workspace.newProjectDescription("Eigenprojectnaam"),new NullProgressMonitor());
					project.create(workspace.newProjectDescription("Eigenprojectnaam"),new NullProgressMonitor());
			}
		
			if(!project.isOpen()) {
					project.open(new NullProgressMonitor());
			}
			InputStream in = new StringBufferInputStream(((LanguageSectionPage)pages[0]).getProjectLanguage());
			projfile.create(in,true,null);
			
			//project nature association
		      IProjectDescription description = project.getDescription();
		     // ChameleonProject champroject = (ChameleonProject)(project.getProject());
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

//					PrintStream out = new PrintStream(new File("temp"));
//					out.println(((LanguageSectionPage)pages[0]).getProjectLanguage());
//					out.close();
		      
		    ChameleonProjectNature chameleonNature = ((ChameleonProjectNature)project.getNature(ChameleonProjectNature.NATURE));
//		    String languageName = ((LanguageSectionPage)pages[0]).getProjectLanguage();
//		    Language language = LanguageMgt.getInstance().createLanguage(languageName);
//			  chameleonNature.init(language);
			  chameleonNature.setProject(project);
			
	} catch (CoreException e) {
		e.printStackTrace();
	}

		return true;
	}

}
