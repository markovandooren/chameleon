/**
 * Created on 14-jun-07
 * @author Tim Vermeiren
 */
package org.aikodi.chameleon.eclipse.view.callhierarchy;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.eclipse.editors.ChameleonEditor;
import org.eclipse.jface.viewers.*;

/**
 * This listener will check if a declaration in the Call Hierarchy View is (double)clicked
 * and if so it will open the declaration in the editor.
 * 
 * @author Tim Vermeiren
 * @author Marko van Dooren
 */
public class MethodClickedListener implements IDoubleClickListener, ISelectionChangedListener {

	/**
	 * This method will show the method selected in the Call Hierarchy in a ChameleonEditor
	 * if the appropriate editor is already opened.
	 */
	@Override
   public void selectionChanged(SelectionChangedEvent event) {
		if(event.getSelection() instanceof IStructuredSelection) {
			IStructuredSelection selection = (IStructuredSelection)event.getSelection();
			Object firstObject = selection.getFirstElement();
			Declaration declaration = null;
			if(firstObject instanceof Declaration){
				declaration = (Declaration)firstObject;
			} else if(firstObject instanceof RootDeclaration){
				declaration = ((RootDeclaration)firstObject).getDeclaration();
			}
			ChameleonEditor.showInEditor(declaration, false, false, null);
		}
	}
	
	/**
	 * This method will open the selected method in the Call hierarchy
	 * in a ChameleonEditor (and open a new editor if necessary) 
	 */
	@Override
   public void doubleClick(DoubleClickEvent event) {
		if(event.getSelection() instanceof IStructuredSelection) {
			IStructuredSelection selection = (IStructuredSelection)event.getSelection();
			Object firstObject = selection.getFirstElement();
			Declaration declaration = null;
			if(firstObject instanceof Declaration){
				declaration = (Declaration)firstObject;
			} else if(firstObject instanceof RootDeclaration){
				declaration = ((RootDeclaration)firstObject).getDeclaration();
			}
			ChameleonEditor.showInEditor(declaration, true, true, null);
		}
	}

}
