/**
 * Created on 13-jun-07
 * @author Tim Vermeiren
 */
package chameleon.eclipse.presentation.callhierarchy;

import java.util.Collection;
import java.util.TreeSet;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.rejuse.predicate.SafePredicate;

import chameleon.core.language.Language;
import chameleon.eclipse.connector.EclipseEditorTag;
import chameleon.eclipse.editors.ChameleonDocument;
import chameleon.eclipse.editors.ChameleonEditor;
import chameleon.eclipse.presentation.treeview.ChameleonLabelProvider;
import chameleon.eclipse.project.ChameleonProjectNature;
import chameleon.oo.method.Method;

/**
 * Will show the callers of the current method in the CallHierarchyView
 * 
 * @author Tim Vermeiren
 *
 */
public abstract class OpenCallHierarchyAction extends Action {

	private CallHierarchyView view;

	public OpenCallHierarchyAction(CallHierarchyView view) {
		this.view = view;
	}

	@Override
	public void run() {
		ChameleonEditor editor = ChameleonEditor.getCurrentActiveEditor();
		if(editor!=null){
			ChameleonDocument doc = editor.getDocument();
			if(doc != null){
				// set labelprovider
				Language lang = doc.language();
				if(lang != null){
					view.getTreeViewer().setLabelProvider(new ChameleonLabelProvider(lang, true, true, false));
				}
				// set content provider
				ChameleonProjectNature projNat = doc.getProjectNature();
				view.getTreeViewer().setContentProvider(getContentProvider(projNat));
			}
		}
		// set input method:
		Method currentMethod = getCurrentMethod();
		view.getTreeViewer().setInput(new RootMethod(currentMethod));

	}
	
	protected abstract IContentProvider getContentProvider(ChameleonProjectNature projectNature);

	/**
	 * Returns the method surrounding the cursor in the current active editor if any, null otherwise.
	 */
	protected Method getCurrentMethod() {
		ChameleonEditor editor = ChameleonEditor.getCurrentActiveEditor();
		if(editor!=null){
			ChameleonDocument doc = editor.getDocument();
			if(doc != null){
				ISelectionProvider selectionProvider = editor.getSelectionProvider();
				if(selectionProvider!=null){
					ISelection sel = selectionProvider.getSelection();
					if (sel instanceof TextSelection) {
						TextSelection textSel = (TextSelection) sel;
						final int offset = textSel.getOffset();
						// build a predicate that checks if the EditorTag includes the offset and is a method:
						SafePredicate<EclipseEditorTag> predicate = new SafePredicate<EclipseEditorTag>(){
							@Override
							public boolean eval(EclipseEditorTag editorTag) {
								return editorTag.includes(offset) && (editorTag.getElement() instanceof Method);
							}
						};
						Collection<EclipseEditorTag> result = new TreeSet<EclipseEditorTag>(EclipseEditorTag.lengthComparator);
						doc.getEditorTagsWithPredicate(predicate, result);
						if(result.size()>0){
							Method currentMethod = (Method)result.iterator().next().getElement();
							return currentMethod;
						}
					}
				}
			}
		}
		return null;
	}
	
	
	

}
