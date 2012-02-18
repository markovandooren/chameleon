/**
 * Created on 3-apr-07
 * @author Tim Vermeiren
 * @author Marko van Dooren
 */
package chameleon.eclipse.editors.actions;

import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;

public interface IChameleonEditorActionDefinitionIds extends ITextEditorActionDefinitionIds {
	
	
	/**
	 * Action definition ID of the format action.
	 * DO NOT CHANGE : This must be the same as used in de chameleon editor plugin.xml file.
	 * (value <code>"chameleon.eclipse.actions.format"</code>).
	 */
	public static final String FORMAT = "chameleon.eclipse.actions.format"; //$NON-NLS-1$
	
	/**
	 * Action definition ID of the "open type in hierarchy" action.
	 * DO NOT CHANGE : This must be the same as used in de chameleon editor plugin.xml file.
	 * (value <code>"chameleon.eclipse.actions.opensubhierarchy"</code>).
	 */
	public static final String SUB_HIERARCHY = "chameleon.eclipse.actions.opensubhierarchy"; //$NON-NLS-1$
	
	/**
	 * Action definition ID of the "open type in hierarchy" action.
	 * DO NOT CHANGE : This must be the same as used in de chameleon editor plugin.xml file.
	 * (value <code>"chameleon.eclipse.actions.opensuperhierarchy"</code>).
	 */
	public static final String SUPER_HIERARCHY = "chameleon.eclipse.actions.opensuperhierarchy"; //$NON-NLS-1$
	
	
}
