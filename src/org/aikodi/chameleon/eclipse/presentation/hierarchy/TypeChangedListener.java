/**
 * Created on 25-apr-07
 * @author Tim Vermeiren
 */
package org.aikodi.chameleon.eclipse.presentation.hierarchy;

import org.aikodi.chameleon.exception.ModelException;
import org.aikodi.chameleon.oo.type.Type;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;

/**
 * When a new type is selected in the hierarhcy viewer, the corresponding
 * members should be shown in the member viewer.
 * 
 * @author Tim Vermeiren
 *
 */
public class TypeChangedListener implements ISelectionChangedListener {
	
	private HierarchyView _hierarchyView;
	
	public TypeChangedListener(HierarchyView hierarchyView) {
		this._hierarchyView = hierarchyView;
	}
	
	protected StructuredViewer getMethodViewer(){
		return _hierarchyView.getMemberViewer();
	}
	
	/**
	 * If selection changed of hierarchytreeview, then
	 * its members will be shown in the memberview
	 */
	@Override
   public void selectionChanged(SelectionChangedEvent event) {
		ISelection selection = event.getSelection();
		selectionChanged(selection);
	}
	
	/**
	 * If selection changed of hierarchytreeview, then
	 * its members will be shown in the memberview
	 */
	public void selectionChanged(ISelection selection) {
		if (selection instanceof StructuredSelection) {
			Object firstElement = ((StructuredSelection)selection).getFirstElement();
			// Element selected in Type Hierarchy
			if(firstElement instanceof HierarchyTypeNode){
				Type type;
				try {
					type = ((HierarchyTypeNode)firstElement).getType();
					getMethodViewer().setInput(type);
				} catch (ModelException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void update() {
		getMethodViewer().refresh();
	}
	
}
