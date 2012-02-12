/**
 * Created on 3-apr-07
 * @author Tim Vermeiren
 */
package chameleon.eclipse.editors.actions;

import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;

public interface IChameleonEditorActionDefinitionIds extends ITextEditorActionDefinitionIds {
	
	
	/**
	 * Action definition ID of the format action.
	 * DO NOT CHANGE : This must be the same than used in de chameleon editor plugin.xml file.
	 * (value <code>"chameleon.editor.actions.format"</code>).
	 */
	public static final String FORMAT = "chameleon.editor.actions.format"; //$NON-NLS-1$
	
	/**
	 * Action definition ID of the "open type in hierarchy" action.
	 * DO NOT CHANGE : This must be the same than used in de chameleon editor plugin.xml file.
	 * (value <code>"chameleon.editor.actions.opensubhierarchy"</code>).
	 */
	public static final String SUB_HIERARCHY = "chameleon.editor.actions.opensubhierarchy"; //$NON-NLS-1$
	
	/**
	 * Action definition ID of the "open type in hierarchy" action.
	 * DO NOT CHANGE : This must be the same than used in de chameleon editor plugin.xml file.
	 * (value <code>"chameleon.editor.actions.opensuperhierarchy"</code>).
	 */
	public static final String SUPER_HIERARCHY = "chameleon.editor.actions.opensuperhierarchy"; //$NON-NLS-1$
	
	
}
