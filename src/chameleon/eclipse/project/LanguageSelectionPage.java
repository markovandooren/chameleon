package chameleon.eclipse.project;

import java.util.ArrayList;

import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

import chameleon.core.language.Language;
import chameleon.eclipse.LanguageMgt;

public class LanguageSelectionPage extends WizardPage {


	protected LanguageSelectionPage(String pageName, ProjectWizard chameleonProjectWizard) {
		super(pageName);
		_chameleonProjectWizard = chameleonProjectWizard;
	}

	private int selected;
	private Composite controlContainer;
	private List optionList;
	
	/**
	 * We also store a list of the available languages as that makes it easier 
	 * to extract the name and the version. 
	 */
	private java.util.List<Language> _languages;


	/**
	 * The page is complete when a language has been selected.
	 */
	public boolean isPageComplete(){
		return optionList.getSelection().length > 0;
	}

	public String getName() {
		return "Select the language of the new project";
	}

	public void createControl(Composite parent) {
		controlContainer = new Composite(parent,SWT.NONE);
		controlContainer.setLayout( new FillLayout(  ) );
		controlContainer.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		createPageContent( );
		controlContainer.setLayout(new GridLayout());	
		setControl(controlContainer);
		setPageComplete(true);
		optionList.addSelectionListener(new SelectionListener(){
			public void widgetSelected(SelectionEvent e) {
				languageSelected();
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				languageSelected();
				getWizard().getContainer().showPage(getNextPage());
			}
			private void languageSelected() {
				ProjectWizard chameleonProjectWizard = chameleonProjectWizard();
				chameleonProjectWizard.getContainer().updateButtons();
				chameleonProjectWizard.createConfig(selectedLanguage());
			}
		}
		);
		
	}
	
	private ProjectWizard chameleonProjectWizard() {
		return _chameleonProjectWizard;
	}

	private final ProjectWizard _chameleonProjectWizard;
	
	private void createPageContent(){
		_languages = new ArrayList<>();
		optionList = new List(controlContainer, SWT.BORDER);

		for(Language lang: LanguageMgt.getInstance().workspace().languageRepository().languages()) {
			String text = lang.name();
			text += " ";
			text += lang.version().toString();
			optionList.add(text);
			_languages.add(lang);
		}
		optionList.setVisible(true);

	}


	public String getDescription() {
		return "Allows the user to select the language of the new project";
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

	/**
	 * Return the lang
	 * @return
	 */
	public Language selectedLanguage(){
		return _languages.get(optionList.getSelectionIndex());
	}

}