package chameleon.eclipse.editors.preferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

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

import chameleon.core.language.Language;
import chameleon.eclipse.ChameleonEditorPlugin;
import chameleon.eclipse.LanguageMgt;
import chameleon.eclipse.presentation.PresentationModel;
import chameleon.eclipse.presentation.formatting.ChameleonFormatterStrategy;

/**
 * @author Tim Vermeiren
 * 
 * A preference page defining the elements used for the auto-indentation. 
 * Users can adjust these settings.
 */
public class FormatterPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	
	/**
	 * For every language (first String) there are a number of boolean options
	 * to (de)select if an element with a certain name (second String) has to be indented
	 */
	private HashMap<String, HashMap<String, BooleanFieldEditor>> options;
	
	/**
	 * creates a new indent preference page.
	 * It contains field editors aligned in a grid.
	 * The preference store that is being used, is de default preference store.
	 */
	public FormatterPreferencePage() {
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
		
		//a vector containing:
		// per language a vector of elements
		// the first element of each vector is the language name
		HashMap<String, List<String[]>> possibilities =  readPossibilities();
		Set<String> languages = possibilities.keySet();

		TabFolder languageTabs = new TabFolder(getFieldEditorParent(),SWT.NONE);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL,true, true);
		languageTabs.setLayoutData( gridData);
		
		for (Iterator<String> iter = languages.iterator(); iter.hasNext();) {
			String taalS = iter.next();

			PresentationModel.initIndentElementsByDefaults(taalS);
			LanguageMgt.getInstance().getPresentationModel(taalS);
			// create Tab page
			TabItem currentPage = new TabItem(languageTabs, SWT.NONE);
			currentPage.setText(taalS);
			// create Scroller
			ScrolledComposite scroller = new ScrolledComposite(languageTabs, SWT.V_SCROLL);
			currentPage.setControl(scroller);
			GridLayout scrollGrid = new GridLayout(1, true);
			scroller.setLayout(scrollGrid);
			// create container (for the checkBoxes)
			Composite container = new Composite(scroller, SWT.NONE);
			scroller.setContent(container);
			GridLayout containerGrid = new GridLayout(1, true);
			container.setLayout(containerGrid);
			
			HashMap<String, BooleanFieldEditor> current = new HashMap<String, BooleanFieldEditor>();
			List<String[]> taal = possibilities.get(taalS);
			for(int i = 0; i< taal.size(); i++){
				String elemNaam = taal.get(i)[0];
				String elemDesc = taal.get(i)[1];
				String fieldNaam = "indentElement_"+taalS+"_"+elemNaam;
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
			container.setSize(scroller.getClientArea().width, scroller.getClientArea().height);
			
			options.put(taalS, current);
		}
	}

	public void init(IWorkbench workbench) {}

	/***************
	 * 
	 * All about reading & writing from the model
	 * 
	 ****************/
	
	/*
	 * reads all languages from the languageMgt
	 * then retrieves all possible language indentation elements
	 */
	private HashMap<String, List<String[]>> readPossibilities() {
	    //lees eerst alle talen uit
		List<String> languages = new ArrayList<String>();
		for(Language lang: LanguageMgt.getInstance().workspace().languageRepository().languages()) {
			languages.add(lang.name());
		}
		//haal van alle talen alle elementen op
	    HashMap<String, List<String[]>> result = obtainLanguageIndentElements(languages);
	    return result;
	}

	/*
	 * obtain the language indent elements for a vector of languages
	 * for each language, the corresponding xml file is read and elements are added 
	 */
	private HashMap<String, List<String[]>> obtainLanguageIndentElements(List<String> talen) {
		HashMap<String, List<String[]>> result = new HashMap<String, List<String[]>>();
		for(String taal: talen){
			List<String[]> taalResult = LanguageMgt.getInstance().getPresentationModel(taal).getIndentElements();
			result.put(taal, taalResult);
		}
		return result;
	}


	/**
	 * all choices are applied and saved
	 */
	public boolean performOk(){
		boolean prev = super.performOk();
		performChoices();		
		return prev;
	}
	
	/*
	 * set per language the indent Elements.
	 * then writes all preferences to the store
	 * 
	 */
	private void performChoices(){
		
		Set<String> languages = options.keySet();
		// iterate over languages:
		for (Iterator<String> iter = languages.iterator(); iter.hasNext();) {
			String language = iter.next();
			
			Vector<String> indentElements = new Vector<String>();
			
			Set<String> elements = options.get(language).keySet();
			// iterate over options
			for (Iterator<String> iterator = elements.iterator(); iterator.hasNext();) {
				String name = iterator.next();
				BooleanFieldEditor field = options.get(language).get(name);
				if (field.getBooleanValue()) indentElements.add(name);
			}
			
			ChameleonFormatterStrategy.setIndentElements(language, indentElements);
		}		
		
		IPreferenceStore store = ChameleonEditorPlugin.getDefault().getPreferenceStore();
		store.setValue("Chameleon_prefs_inited", true);
	}
	
	public void performApply(){
		super.performApply();
		performChoices();
	}	


}
