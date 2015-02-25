package org.aikodi.chameleon.eclipse.project;

import java.util.ArrayList;

import org.aikodi.chameleon.core.language.Language;
import org.aikodi.chameleon.eclipse.LanguageMgt;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

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
	@Override
   public boolean isPageComplete(){
		return optionList.getSelection().length > 0;
	}

	@Override
   public String getName() {
		return "Select the language of the new project";
	}

	@Override
   public void createControl(Composite parent) {
		controlContainer = new Composite(parent,SWT.NONE);
		controlContainer.setLayout( new FillLayout(  ) );
		controlContainer.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		createPageContent( );
		controlContainer.setLayout(new GridLayout());	
		setControl(controlContainer);
		setPageComplete(true);
		optionList.addSelectionListener(new SelectionListener(){
			@Override
         public void widgetSelected(SelectionEvent e) {
				languageSelected();
			}
			@Override
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
		_languages = new ArrayList<Language>();
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


	@Override
   public String getDescription() {
		return "Allows the user to select the language of the new project";
	}

	@Override
   public String getErrorMessage() {
		if (selected<0) return "No Language selected";
		return null; 
	}

	@Override
   public String getMessage() {
		return "Please select the language to use for this project";
	}

	@Override
   public String getTitle() {
		return "Language Selection";
	}


	@Override
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
