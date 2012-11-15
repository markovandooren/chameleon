package chameleon.eclipse.project;

import java.io.File;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import chameleon.workspace.ProjectConfig;

public class ProjectDetailsPage extends WizardPage implements IWizardPage {
//public class ProjectDetailsPage extends WizardNewProjectCreationPage implements IWizardPage {

	public ProjectDetailsPage(String pageName, ProjectWizard chameleonProjectWizard) {
		super(pageName);
		_chameleonProjectWizard = chameleonProjectWizard;
	}
	
	

	private ProjectWizard chameleonProjectWizard() {
		return _chameleonProjectWizard;
	}

	private final ProjectWizard _chameleonProjectWizard;
	
	public Text projectTitle;
	
	public Text projectPath;

	private Composite controlContainer;

	public void createControl(Composite parent) {
		controlContainer = new Composite(parent,SWT.NONE);
		GridLayout gl = new GridLayout();
		controlContainer.setLayout(gl);
		projectTitle = new Text(controlContainer,SWT.BORDER);
		projectTitle.setBounds(0,55,300,15);
		projectTitle.setLayoutData(new GridData(55,15));

		projectTitle.setText("MyProject");
		projectTitle.addModifyListener(new ModifyListener(){
		
			@Override
			public void modifyText(ModifyEvent arg0) {
				syncName();
			}
		});
		
		projectPath = new Text(controlContainer,SWT.BORDER);
		projectPath.setText("MyProject");
		projectPath.addModifyListener(new ModifyListener(){
		
			@Override
			public void modifyText(ModifyEvent arg0) {
				syncRoot();
			}
		});

		setControl(controlContainer);

		getControl().addFocusListener(new FocusListener(){
			
			@Override
			public void focusLost(FocusEvent arg0) {
			}
		
			@Override
			public void focusGained(FocusEvent arg0) {
				syncName();
				syncRoot();
			}

		});
	}

	protected void syncName() {
		chameleonProjectWizard().projectConfig().setName(projectName());
	}
	
	protected void syncRoot() {
		File root = new File(projectPath.getText());
		chameleonProjectWizard().projectConfig().setRoot(root);
	}

	//	public void setVisible(boolean visible) {
//		controlContainer.setVisible(visible);
//		super.setVisible(visible);
//	}

	public String projectName() {
		return projectTitle.getText();
	}		

}