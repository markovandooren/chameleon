package org.aikodi.chameleon.eclipse.editors.preferences;


import org.aikodi.chameleon.core.language.Language;
import org.aikodi.chameleon.eclipse.ChameleonEditorPlugin;
import org.aikodi.chameleon.eclipse.LanguageMgt;
import org.aikodi.chameleon.eclipse.presentation.PresentationModel;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import java.util.*;


/**
 * @author Manuel Van Wesemael 
 * @author Joeri Hendrickx 
 * 
 *	A preference page defining the possible outline elements that will be available
 *  for the outline of the available languages. Users can adjust these settings.
 */
public class OutlinePreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	/**
	 * For every language (first String) there are a number of boolean options
	 * to (de)select if an element with a certain name (second String) has to be shown in the outline.
	 */
	private HashMap<String, HashMap<String, BooleanFieldEditor>> options;
	
	
	/**
	 * creates a new outline preference page.
	 * It contains field editors aligned in a grid.
	 * The preference store that is being used, is de default preference store.
	 */
	public OutlinePreferencePage() {
		super(FieldEditorPreferencePage.GRID);
		
		// Set the preference store for the preference page.
		IPreferenceStore store = ChameleonEditorPlugin.getDefault().getPreferenceStore();
		setPreferenceStore(store);
	}

	@Override
	/**
	 * creates the elements for the page.
	 * It creates the appropriate number of editor where users can choose whether an
	 * element is allowed or not.
	 * Each language gets an own tab.
	 */
	protected void createFieldEditors() {
		
		options = new HashMap<String, HashMap<String, BooleanFieldEditor>>();
		
		//a hashmap containing:
		// per language a vector of elements
		// the first element of each vector is the language name
		HashMap<String, List<String[]>> possibilities =  readPossibilities();
		Set<String> languages = possibilities.keySet();

		TabFolder languageTabs = new TabFolder(getFieldEditorParent(),SWT.NONE);
		languageTabs.setLayoutData(new GridData(SWT.FILL, SWT.FILL,true, true));
		
		
		for (Iterator<String> iter = languages.iterator(); iter.hasNext();) {
			String taalS = iter.next();

			PresentationModel.initAllowedOutlineElementsByDefaults(taalS);
			LanguageMgt.getInstance().getPresentationModel(taalS);

			TabItem currentPage = new TabItem(languageTabs, SWT.NONE);
			currentPage.setText(taalS);
			ScrolledComposite scroller = new ScrolledComposite(languageTabs, SWT.V_SCROLL);
			currentPage.setControl(scroller);
			Composite container = new Composite(scroller, SWT.NONE);
			scroller.setContent(container);
			GridLayout grid = new GridLayout(1, true);
			container.setLayout(grid);
			
			HashMap<String, BooleanFieldEditor> current = new HashMap<String, BooleanFieldEditor>();
			List<String[]> taal = possibilities.get(taalS);
			for(int i = 0; i< taal.size(); i++){
				String elemNaam = taal.get(i)[0];
				String elemDesc = taal.get(i)[1];
				String fieldNaam = "outlineElement_"+taalS+"_"+elemNaam;
				BooleanFieldEditor formatOnSave = new BooleanFieldEditor(
						fieldNaam, 
						elemDesc, 
						container);
				addField(formatOnSave);
				current.put(elemNaam,formatOnSave);
			}
			container.pack();
			scroller.pack();
			languageTabs.setSize(getFieldEditorParent().getClientArea().width,getFieldEditorParent().getClientArea().height);
			options.put(taalS, current);
			 
		}
	}



	@Override
   public void init(IWorkbench workbench) {}

	/***************
	 * 
	 * All about reading & writing from the model
	 * 
	 ****************/
	
	/*
	 * reads all languages from the languageMgt
	 * then retrieves all possible language outline elements
	 */
	private HashMap<String, List<String[]>> readPossibilities() {
	    //lees eerst alle talen uit
		List<String> languages = new ArrayList<String>();
		for(Language lang: LanguageMgt.getInstance().workspace().languageRepository().languages()) {
			languages.add(lang.name());
		}
		
		//haal van alle talen alle elementen op
	    HashMap<String, List<String[]>> result = obtainLanguageOutlineElements(languages);
	    return result;
	}

	/*
	 * obtain the language outline elements for a vector of languages
	 * for each language, the corresponding xml file is read and elements are added 
	 */
	private HashMap<String, List<String[]>> obtainLanguageOutlineElements(List<String> languages) {
		HashMap<String, List<String[]>> result = new HashMap<String, List<String[]>>();
		for(String language: languages){
			List<String[]> languageResult = LanguageMgt.getInstance().getPresentationModel(language).getOutlineElements();
			result.put(language, languageResult);
		}
		return result;
	}


	/**
	 * all choices are applied and saved & the necessary outlines are updated
	 */
	@Override
   public boolean performOk(){
		boolean prev = super.performOk();
		performChoices();		
		return prev;
	}
	
	/*
	 * set per language the allowed outlineTreeElements.
	 * then writes all preferences to the store
	 * then updates all outlines 
	 * 
	 */
	private void performChoices(){
		
//		Set<String> languages = options.keySet();
		// iterate over languages:
//		for (String language: languages) {
//			List<String> allowed = new ArrayList<String>();
			
//			Set<String> elements = options.get(language).keySet();
			// iterate over options:
//			for (Iterator<String> iterator = elements.iterator(); iterator.hasNext();) {
//				String name = iterator.next();
//				BooleanFieldEditor field = options.get(language).get(name);
//				if (field.getBooleanValue()) {
//					allowed.add(name);
//				}
//			}
			
//			ChameleonOutlineTree.setAllowedElements(language, allowed);
//		}		
		
		IPreferenceStore store = ChameleonEditorPlugin.getDefault().getPreferenceStore();
		store.setValue("Chameleon_prefs_inited", true);
		
		//FIXME update all outlines
//		ChameleonOutlinePage.updateAll();
	}
	
	@Override
   public void performApply(){
		super.performApply();
		performChoices();
		
		//ChameleonOutlineTree.setAllowedElements(lang, allowed);
	}	


}
