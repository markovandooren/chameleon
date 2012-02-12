package chameleon.eclipse.editors.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import chameleon.eclipse.ChameleonEditorPlugin;
import chameleon.eclipse.presentation.autocompletion.AutoCompletionProposalsComparator;
import chameleon.eclipse.presentation.formatting.ChameleonAutoEditStrategy;

/**
 * @author Manuel Van Wesemael 
 * @author Joeri Hendrickx 
 * 
 * The Root for the preference pages of the editor. No options are defined yet.
 */
public class ChameleonEditorPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String NB_AUTO_COMPLETION_TYPE_SEARCH_LEVELS = "nbAutoCompletionTypeSearchLevels";
	public static final int DEFAULT_NB_AUTO_COMPLETION_TYPE_SEARCH_LEVELS = 1;
	
	public static final String ENABLE_AUTO_FORMATTING = "enableAutoFormatting";

	public ChameleonEditorPreferencePage() {
		super(FieldEditorPreferencePage.GRID);
		
		// Set the preference store for the preference page.
		IPreferenceStore store = ChameleonEditorPlugin.getDefault().getPreferenceStore();
		setPreferenceStore(store);
		initDefaults();
	}

	/**
	 * The number of levels the auto-completion processor will search for
	 * matching types
	 */
	private IntegerFieldEditor nbAutoCompletionTypeSearchLevels;

	private Group autocompletionGroup;
	
	private Group autoformattingGroup;

	private BooleanFieldEditor booleanFieldEditor;

	@Override
	protected void createFieldEditors() {
		// create auto-completion group:
		autocompletionGroup = new Group(getFieldEditorParent(), SWT.SHADOW_NONE);
		autocompletionGroup.setLayout(new GridLayout(1, true));
		autocompletionGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		autocompletionGroup.setText("Auto-completion");
		// add integer field:
		nbAutoCompletionTypeSearchLevels = new IntegerFieldEditor(
				NB_AUTO_COMPLETION_TYPE_SEARCH_LEVELS, 
				"Number of levels for typechecking in auto-completion", 
				autocompletionGroup);
		nbAutoCompletionTypeSearchLevels.setValidRange(0, 5);
		nbAutoCompletionTypeSearchLevels.setTextLimit(1);
		addField(nbAutoCompletionTypeSearchLevels);
		
		// create auto-formatting group:
		autoformattingGroup = new Group(getFieldEditorParent(), SWT.SHADOW_NONE);
		autoformattingGroup.setLayout(new GridLayout(1, true));
		autoformattingGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		autoformattingGroup.setText("Auto-formatting");
		// create auto-formatting checkbox:
		booleanFieldEditor = new BooleanFieldEditor(ENABLE_AUTO_FORMATTING, "Enable Auto-formatting while typing", autoformattingGroup);
		addField(booleanFieldEditor);
		
		}

	public void init(IWorkbench workbench) {
		
	}
	
	public boolean performOk(){
		boolean prev = super.performOk();
		performChoices();		
		return prev;
	}
	
	private void performChoices() {
		AutoCompletionProposalsComparator.nbOfDefiningTypeChecks = getPreferenceStore().getInt(NB_AUTO_COMPLETION_TYPE_SEARCH_LEVELS);
		ChameleonAutoEditStrategy.ENABLE_AUTO_FORMATTING = getPreferenceStore().getBoolean(ENABLE_AUTO_FORMATTING);
	}

	public void performApply(){
		super.performApply();
		performChoices();
	}

	private static void initDefaults() {
		IPreferenceStore store = ChameleonEditorPlugin.getDefault().getPreferenceStore();
		if( ! store.contains(NB_AUTO_COMPLETION_TYPE_SEARCH_LEVELS)) {
			store.setValue(NB_AUTO_COMPLETION_TYPE_SEARCH_LEVELS, DEFAULT_NB_AUTO_COMPLETION_TYPE_SEARCH_LEVELS);
		}
		store.setDefault(NB_AUTO_COMPLETION_TYPE_SEARCH_LEVELS, DEFAULT_NB_AUTO_COMPLETION_TYPE_SEARCH_LEVELS);
		
		if( ! store.contains(ENABLE_AUTO_FORMATTING)) {
			store.setValue(ENABLE_AUTO_FORMATTING, true);
		}
		store.setDefault(ENABLE_AUTO_FORMATTING, true);
		
	}
	
	@Override
	protected void adjustGridLayout() {
		// don't add columns (like in super method)
	}


}
