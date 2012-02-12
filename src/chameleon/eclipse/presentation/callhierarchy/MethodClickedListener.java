/**
 * Created on 14-jun-07
 * @author Tim Vermeiren
 */
package chameleon.eclipse.presentation.callhierarchy;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;

import chameleon.eclipse.editors.ChameleonEditor;
import chameleon.oo.method.Method;

/**
 * This listener will check if a method in the Call Hierarchy View is (double)clicked
 * and if so it will open the method in the editor.
 * 
 * @author Tim Vermeiren
 *
 */
public class MethodClickedListener implements IDoubleClickListener,
		ISelectionChangedListener {

	/**
	 * This method will show the method selected in the Call Hierarchy in a ChameleonEditor
	 * if the appropriate editor is already opened.
	 */
	public void selectionChanged(SelectionChangedEvent event) {
		if(event.getSelection() instanceof IStructuredSelection) {
			IStructuredSelection selection = (IStructuredSelection)event.getSelection();
			Object firstObject = selection.getFirstElement();
			if(firstObject instanceof Method){
				Method method = (Method)firstObject;
				ChameleonEditor.showInEditor(method, false, false, null);
			} else if(firstObject instanceof RootMethod){
				Method method = ((RootMethod)firstObject).getMethod();
				ChameleonEditor.showInEditor(method, false, false, null);
			}
		}
	}
	
	/**
	 * This method will open the selected method in the Call hierarchy
	 * in a ChameleonEditor (and open a new editor if necessary) 
	 */
	public void doubleClick(DoubleClickEvent event) {
		if(event.getSelection() instanceof IStructuredSelection) {
			IStructuredSelection selection = (IStructuredSelection)event.getSelection();
			Object firstObject = selection.getFirstElement();
			if(firstObject instanceof Method){
				Method method = (Method)firstObject;
				ChameleonEditor.showInEditor(method, true, true, null);
			} else if(firstObject instanceof RootMethod){
				Method method = ((RootMethod)firstObject).getMethod();
				ChameleonEditor.showInEditor(method, true, true, null);
			}
		}
	}

}
