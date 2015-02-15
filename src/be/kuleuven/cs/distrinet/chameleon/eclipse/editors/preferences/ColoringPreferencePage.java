package be.kuleuven.cs.distrinet.chameleon.eclipse.editors.preferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.internal.WorkbenchMessages;

import be.kuleuven.cs.distrinet.chameleon.core.language.Language;
import be.kuleuven.cs.distrinet.chameleon.eclipse.ChameleonEditorPlugin;
import be.kuleuven.cs.distrinet.chameleon.eclipse.LanguageMgt;
import be.kuleuven.cs.distrinet.chameleon.eclipse.editors.ChameleonEditor;
import be.kuleuven.cs.distrinet.chameleon.eclipse.presentation.PresentationStyle;
import be.kuleuven.cs.distrinet.chameleon.eclipse.presentation.Selector;

/**
 * @author Manuel Van Wesemael 
 * @author Joeri Hendrickx 
 * 
 * A preference page for chameleonDocuments.
 * It handles the colors used in the syntax coloring for the various languages.
 * The colors can be changed at will.
 */
public class ColoringPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	//per language, contains a color selector and the field editor that represents it
	private HashMap<String, HashMap<Selector, Vector<FieldEditor>>> options;

	public ColoringPreferencePage() {
		super(FieldEditorPreferencePage.GRID);
		// Set the preference store for the preference page.
		IPreferenceStore store = ChameleonEditorPlugin.getDefault().getPreferenceStore();
		setPreferenceStore(store);
	}

	/*
	 * stores the chosen colors and recolors all the documents
	 */
	private void performChoices(){
		
		for (String language : options.keySet()) {
			
			HashMap<Selector, Vector<FieldEditor>> fieldsBySelector = options.get(language);
			
			Set<Selector> selectors = fieldsBySelector.keySet();
			
			for (Iterator<Selector> iterator = selectors.iterator(); iterator.hasNext();) {
				Selector selector = iterator.next();
				OptionalColorFieldEditor fg = (OptionalColorFieldEditor) fieldsBySelector.get(selector).get(0);
				OptionalColorFieldEditor bg = (OptionalColorFieldEditor) fieldsBySelector.get(selector).get(1);
				BooleanFieldEditor bold = (BooleanFieldEditor) fieldsBySelector.get(selector).get(2);
				BooleanFieldEditor italic = (BooleanFieldEditor) fieldsBySelector.get(selector).get(3);
				BooleanFieldEditor underline = (BooleanFieldEditor) fieldsBySelector.get(selector).get(4);
				BooleanFieldEditor foldable = (BooleanFieldEditor) fieldsBySelector.get(selector).get(5);
				BooleanFieldEditor folded = (BooleanFieldEditor) fieldsBySelector.get(selector).get(6);
				PresentationStyle style = new PresentationStyle(fg.getOptionalColor(), bg.getOptionalColor(), bold.getBooleanValue(), italic.getBooleanValue(), underline.getBooleanValue(), foldable.getBooleanValue(), folded.getBooleanValue());
				// FIXME: this should update the presentation models of all project natures.
				LanguageMgt.getInstance().getPresentationModel(language).updateRule(selector, style);
			}
			
			
		}		
		
		IPreferenceStore store = ChameleonEditorPlugin.getDefault().getPreferenceStore();
		store.setValue("Chameleon_color_prefs_inited", true);
		
		recolorAll();
		
		
	}
	
	//recolors all open editors.
	private void recolorAll() {
		SafeRunner.run(new SafeRunnable(WorkbenchMessages.ErrorClosing) {
			IWorkbench workbench = _workbench;
			@Override
         public void run() {
				// Collect dirtyParts
				//ArrayList dirtyParts = new ArrayList();
				//ArrayList dirtyEditorsInput = new ArrayList();
				IWorkbenchWindow windows[] = workbench.getWorkbenchWindows();
				for (int i = 0; i < windows.length; i++) {
					IWorkbenchPage pages[] = windows[i].getPages();
					for (int j = 0; j < pages.length; j++) {
						IWorkbenchPage page = pages[j];
						
						IEditorReference[] parts = page.getEditorReferences();
                        
                        for (int k = 0; k < parts.length; k++) {
                        	IEditorPart editor = parts[k].getEditor(true);
                        	try{
                        		ChameleonEditor chamEditor = (ChameleonEditor) editor;
                        		chamEditor.updateFoldingStructure();
                        		chamEditor.getDocument().doPresentation(chamEditor.getChameleonConfiguration().getChameleonPresentationReconciler().getTextViewer());
                        		
                        	}catch(ClassCastException cce){
                        		// the part is not a chameleonEditor. No changes are needed
                        	}
                        	
                        }
					}
				}
			}
                            
		});
	}

	@Override
	protected void createFieldEditors() {
		
		

		options = new HashMap<String, HashMap<Selector, Vector<FieldEditor>>>();
		
		//a vector containing:
		// per language a vector of elements
		// the first element of each vector is the language name
		HashMap<String, HashMap<Selector, PresentationStyle>> possibilities =  readConfigurables();
		
		TabFolder languageTabs = new TabFolder(getFieldEditorParent(),SWT.NONE);
		languageTabs.setLayoutData(new GridData(SWT.FILL, SWT.FILL,true, true));
		languageTabs.setLayout(new GridLayout(1, true));
		Set<String> talen = possibilities.keySet();
		
		for (Iterator<String> iter = talen.iterator(); iter.hasNext();) {

			String taalS = iter.next();

			TabItem currentPage = new TabItem(languageTabs, SWT.NONE);
			currentPage.setText(taalS);
			ScrolledComposite scroller = new ScrolledComposite(languageTabs, SWT.V_SCROLL);
			currentPage.setControl(scroller);
			scroller.setLayoutData(new GridData(SWT.FILL, SWT.FILL,true, true));
			scroller.setLayout(new GridLayout(1, true));	
			Composite container = new Composite(scroller, SWT.BORDER);
			scroller.setContent(container);
			GridLayout grid = new GridLayout(2, true);
			container.setLayout(grid);
			container.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false));
			
			HashMap<Selector, Vector<FieldEditor>> current = new HashMap<Selector, Vector<FieldEditor>>();

			HashMap<Selector, PresentationStyle> taal = possibilities.get(taalS);
			Set<Selector> selectors = taal.keySet();
			for (Iterator<Selector> iterator = selectors.iterator(); iterator.hasNext();) {
				
				Group selectorGroup = new Group(container,SWT.NONE);
				GridLayout grid2 = new GridLayout();
				selectorGroup.setLayout(grid2);
				Composite selectorGroupC = new Composite(selectorGroup, SWT.NONE);
				selectorGroupC.setLayout(new GridLayout(2, true));
				selectorGroup.setLayoutData(new GridData(SWT.FILL,SWT.DEFAULT, true, false));
				selectorGroupC.setLayoutData(new GridData(SWT.FILL,SWT.DEFAULT, true, false));
				
				Composite c = selectorGroupC;
				
				Vector<FieldEditor> current2 = new Vector<FieldEditor>();
				Selector selector = iterator.next();

				// PresentationStyle presrule = taal.get(selector);
				String fieldNaam = "stylerule_"+taalS+"_"+selector.getElementType()+"_"+selector.getPositionType()+"_";
//				PresentationStyle presrule = taal.get(selector);
//				String fieldNaam = "stylerule_"+taalS+"_"+selector.getElementType()+"_"+selector.getDecoratorType()+"_";
				
				//Label elementLb = new Label(c, SWT.NONE);
				selectorGroup.setText(selector.getDescription());
				//addField(spacer2);
				PresentationStyle style = taal.get(selector);
				OptionalColorFieldEditor foregroundEdit = new OptionalColorFieldEditor(fieldNaam+"foreground", "Foreground", c);
				foregroundEdit.setOptionalColor(style.getForeground());
				//foregroundEdit.fillIntoGrid(c,2);
				
				
				
				//addField(spacer3);
				OptionalColorFieldEditor backgroundEdit = new OptionalColorFieldEditor(fieldNaam+"background","Background", c);
				backgroundEdit.setOptionalColor(style.getBackground());
				//addField(spacer1);
				BooleanFieldEditor bold = new BooleanFieldEditor(
						fieldNaam+"bold", 
						"Bold", 
				 		new Composite(c,SWT.NONE));
				BooleanFieldEditor italic= new BooleanFieldEditor(
						fieldNaam+"italic", 
						"Italic", 
						new Composite(c,SWT.NONE));
				BooleanFieldEditor underline = new BooleanFieldEditor(
						fieldNaam+"underline", 
						"Underline", 
						new Composite(c,SWT.NONE));
				BooleanFieldEditor foldable = new BooleanFieldEditor(
						fieldNaam+"foldable", 
						"Foldable", 
						new Composite(c,SWT.NONE));
				BooleanFieldEditor folded = new BooleanFieldEditor(
						fieldNaam+"folded", 
						"Folded", 
						new Composite(c,SWT.NONE));				
				addField(foregroundEdit);
				addField(backgroundEdit);
				addField(bold);
				addField(italic);
				addField(underline);
				addField(foldable);
				addField(folded);
				
				current2.add(foregroundEdit);
				current2.add(backgroundEdit);
				current2.add(bold);
				current2.add(italic);
				current2.add(underline);
				current2.add(foldable);
				current2.add(folded);
				current.put(selector, current2);
				
				c.pack();
				selectorGroup.pack();
				
			}
			options.put(taalS, current);
			container.pack();
			scroller.pack();
		}

	}

	@Override
   public void init(IWorkbench workbench) {
		_workbench = workbench;
	}
	
	private IWorkbench _workbench;
	
	//reads the configuration for a specific language and gets it from the xml file
	private HashMap<String, HashMap<Selector, PresentationStyle>> readConfigurables() {
	    //lees eerst alle talen uit
		List<String> languages = new ArrayList<String>();
		for(Language lang: LanguageMgt.getInstance().workspace().languageRepository().languages()) {
			languages.add(lang.name());
		}
		//haal van alle talen alle elementen op
	    HashMap<String, HashMap<Selector, PresentationStyle>> result = obtainLanguageColorElements(languages);
	    return result;
	}

	private HashMap<String, HashMap<Selector, PresentationStyle>> obtainLanguageColorElements(List<String> talen) {
		HashMap<String, HashMap<Selector, PresentationStyle>> result = new HashMap<String, HashMap<Selector, PresentationStyle>>();
		for(String taal: talen){
			HashMap<Selector,PresentationStyle> taalResult = LanguageMgt.getInstance().getPresentationModel(taal).getRules();
			result.put(taal, taalResult);
		}
		return result;
	}

	@Override
   public boolean performOk(){
		boolean prev= super.performOk();
		performChoices();

		return prev;
	}
	
	@Override
   public void performApply(){
		super.performApply();
		
		performChoices();
		
		//ChameleonOutlineTree.setAllowedElements(lang, allowed);
	}

}
