/**
 * Created on 13-jun-07
 * @author Tim Vermeiren
 */
package org.aikodi.chameleon.eclipse.view.callhierarchy;

import java.util.Collection;
import java.util.TreeSet;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.language.Language;
import org.aikodi.chameleon.eclipse.connector.EclipseEditorTag;
import org.aikodi.chameleon.eclipse.editors.ChameleonEditor;
import org.aikodi.chameleon.eclipse.editors.EclipseDocument;
import org.aikodi.chameleon.eclipse.presentation.treeview.ChameleonLabelProvider;
import org.aikodi.chameleon.eclipse.project.ChameleonProjectNature;
import org.aikodi.rejuse.predicate.SafePredicate;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;

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
		Language lang = ChameleonEditor.getActiveLanguage();
		EclipseDocument doc = ChameleonEditor.getActiveDocument();
		// set content provider
		ChameleonProjectNature projNat = doc.getProjectNature();
		view.getTreeViewer().setContentProvider(getContentProvider(projNat));
		// set input method:
		Declaration currentDeclaration = getCurrentDeclaration();
		view.getTreeViewer().setInput(new RootDeclaration(currentDeclaration));
		if(lang != null){
			view.getTreeViewer().setLabelProvider(new ChameleonLabelProvider(lang, true, true, false));
		}

	}
	
	protected abstract IContentProvider getContentProvider(ChameleonProjectNature projectNature);

	/**
	 * Returns the method surrounding the cursor in the current active editor if any, null otherwise.
	 */
	protected Declaration getCurrentDeclaration() {
		ChameleonEditor editor = ChameleonEditor.getActiveEditor();
		if(editor!=null){
			EclipseDocument doc = editor.getDocument();
			if(doc != null){
				ISelectionProvider selectionProvider = editor.getSelectionProvider();
				if(selectionProvider!=null){
					ISelection sel = selectionProvider.getSelection();
					if (sel instanceof TextSelection) {
						TextSelection textSel = (TextSelection) sel;
						final int offset = textSel.getOffset();
						// build a predicate that checks if the EditorTag includes the offset and is a declaration:
						SafePredicate<EclipseEditorTag> predicate = new SafePredicate<EclipseEditorTag>(){
							@Override
							public boolean eval(EclipseEditorTag editorTag) {
								return editorTag.includes(offset) && (editorTag.element() instanceof Declaration);
							}
						};
						Collection<EclipseEditorTag> result = new TreeSet<EclipseEditorTag>(EclipseEditorTag.lengthComparator);
						doc.getEditorTagsWithPredicate(predicate, result);
						if(result.size()>0){
							Declaration currentDeclaration = (Declaration)result.iterator().next().element();
							return currentDeclaration;
						}
					}
				}
			}
		}
		return null;
	}
	
	
	

}
