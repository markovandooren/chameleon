/**
 * Created on 22-jun-07
 * @author Tim Vermeiren
 */
package chameleon.eclipse.presentation.callhierarchy;

import org.eclipse.jface.viewers.IContentProvider;

import chameleon.eclipse.ChameleonEditorPlugin;
import chameleon.eclipse.project.ChameleonProjectNature;

/**
 * This action will open the hierarchy of the callers (the methods that call this method)
 * 
 * @author Tim Vermeiren
 */
public class OpenCalleesHierarchyAction extends OpenCallHierarchyAction {

	public OpenCalleesHierarchyAction(CallHierarchyView view) {
		super(view);
		setText("Open Callees Hierarchy");
		setImageDescriptor(ChameleonEditorPlugin.getImageDescriptor("call_hierarchy_callees.gif"));
	}

	@Override
	protected IContentProvider getContentProvider(ChameleonProjectNature projectNature) {
		return new CalleesContentProvider();
	}
	
	
	
}
