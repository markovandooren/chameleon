/**
 * Created on 22-jun-07
 * @author Tim Vermeiren
 */
package be.kuleuven.cs.distrinet.chameleon.eclipse.presentation.callhierarchy;

import org.eclipse.jface.viewers.IContentProvider;

import be.kuleuven.cs.distrinet.chameleon.eclipse.ChameleonEditorPlugin;
import be.kuleuven.cs.distrinet.chameleon.eclipse.project.ChameleonProjectNature;

/**
 * This action will open the hierarchy of the callers (the methods that call this method)
 * 
 * @author Tim Vermeiren
 */
public class OpenCallersHierarchyAction extends OpenCallHierarchyAction {

	public OpenCallersHierarchyAction(CallHierarchyView view) {
		super(view);
		setText("Open Callers Hierarchy");
		setImageDescriptor(ChameleonEditorPlugin.getImageDescriptor("call_hierarchy_callers.gif"));
	}

	@Override
	protected IContentProvider getContentProvider(ChameleonProjectNature projectNature) {
		return new CallersContentProvider(projectNature);
	}
	
	
	
}
