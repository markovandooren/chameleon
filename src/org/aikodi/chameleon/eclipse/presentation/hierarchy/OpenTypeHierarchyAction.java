/**
 * Created on 22-apr-07
 * @author Tim Vermeiren
 */
package org.aikodi.chameleon.eclipse.presentation.hierarchy;

import java.util.Collection;
import java.util.TreeSet;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.language.Language;
import org.aikodi.chameleon.eclipse.ChameleonEditorPlugin;
import org.aikodi.chameleon.eclipse.connector.EclipseEditorTag;
import org.aikodi.chameleon.eclipse.editors.ChameleonEditor;
import org.aikodi.chameleon.eclipse.editors.EclipseDocument;
import org.aikodi.chameleon.eclipse.presentation.treeview.ChameleonLabelProvider;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.exception.ModelException;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeReference;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;

import be.kuleuven.cs.distrinet.rejuse.predicate.SafePredicate;

/**
 * This class represents the action to open the super or subtype (==actionType in constructor) hierarchy.
 * 
 * It also represents the Listeners of the Hierarchy Viewer and Member viewer to show the selected element
 * in an editor. And when doubleclicked on an element, a new editor will be opened if necessary.
 * 
 * @author Tim Vermeiren
 *
 */
public class OpenTypeHierarchyAction extends Action implements IDoubleClickListener, ISelectionChangedListener {

	private HierarchyView view;
	//protected IContentProvider contentProvider;
	private String actionName;

	/**
	 * Wheter this is a SUBTYPEHIERARCHY or a SUPERTYPEHIERARCHY
	 */
	private int hierarchy_type;

	public static final int SUBTYPE = 0;
	public static final int SUPERTYPE = 1;
	
	/**
	 * 
	 * @param 	actionType
	 * 			must be SUBTYPE or SUPERTYPE
	 * @param 	view
	 * 			the hierarchy view class reference
	 */
	public OpenTypeHierarchyAction(int actionType, HierarchyView view) {
		super("Open Type hierarchy", AS_RADIO_BUTTON);
		this.view = view;
		String iconName;
		String description;
		if(actionType == SUBTYPE){
			this.actionName = "Sub Type hierarchy";
			description = "Open the sub-type hierarchy of the type selected in the editor";
			iconName = "subtype_hierarchy.gif";
		} else if(actionType == SUPERTYPE){
			this.actionName = "Super Type hierarchy";
			description = "Open the super-type hierarchy of the type selected in the editor";
			iconName = "supertype_hierarchy.gif";
		} else {
			throw new ChameleonProgrammerException("Action type not supported. Must be SUBTYPEHIERARCHY or SUPERTYPEHIERARCHY");
		}
		this.hierarchy_type = actionType;
		setText(description);
		setToolTipText(description);
		setDescription(description);
		setImageDescriptor(ChameleonEditorPlugin.getImageDescriptor(iconName));
	}

	@Override
	public void run() {
		Type rootType = getRootType();
		ChameleonEditor editor = ChameleonEditor.getActiveEditor();
		setEditor(editor);
		if(rootType != null && editor != null){
			Language lang = editor.getDocument().language();
			IContentProvider contentProvider;
			if(hierarchy_type == SUBTYPE){
				contentProvider = new SubTypeHierarchyContentProvider(editor.getDocument().getProjectNature().getModel()); // local search
			} else if(hierarchy_type == SUPERTYPE){
				contentProvider = new SuperTypeHierarchyContentProvider();
			} else {
				throw new ChameleonProgrammerException("Action type not supported. Must be SUBTYPEHIERARCHY or SUPERTYPEHIERARCHY");
			}
			view.getHierarchyViewer().setContentProvider(contentProvider);
			view.getHierarchyViewer().setLabelProvider(new ChameleonLabelProvider(lang, false, false, false));
			view.getMemberViewer().setLabelProvider(new ChameleonLabelProvider(lang, true, false, false));
			view.getHierarchyViewer().setInput(new RootType(rootType, getEditor()));
			view.getHierarchyViewer().setSelection(new StructuredSelection(rootType));
			view.getLabel().setText(actionName + " of type " + rootType.getFullyQualifiedName());
		} else{
			if(editor == null){
				ChameleonEditorPlugin.showMessageBox("No Chameleon editor found", "The currently active editor must be a Chameleon editor.\nThe Chameleon Type Hierarchy cannot be shown.", SWT.ICON_ERROR);
			} else if(rootType==null){
				ChameleonEditorPlugin.showMessageBox("No type found", "There is no type found whose type hierarchy must be shown.\nFirst select an appropriate type in the Chameleon editor.", SWT.ICON_ERROR);
			} 
		}
	}
	
	/**
	 * Returns the type to open the hierarchy of.
	 * 
	 * If a typereference is selected, this type is used.
	 * Otherwise the type surrounding the selection is used.
	 * Otherwise the topleveltype of the current document is used.
	 */
	private Type getRootType() {
		try {
			ChameleonEditor editor = getCurrentEditor();
			setEditor(editor);
			if(editor != null ){
				ChameleonEditor chamEditor = editor;
				EclipseDocument doc = chamEditor.getDocument();
				ISelectionProvider selectionProvider = chamEditor.getSelectionProvider();
				if(selectionProvider != null){
					ISelection sel = selectionProvider.getSelection();
					if(sel instanceof TextSelection){
						TextSelection textSel = (TextSelection)sel;
						// get type editor tag around selection with predicate
						final int offset = textSel.getOffset();
						// build a predicate that checks if the EditorTag includes the offset and is a type(reference):
						SafePredicate<EclipseEditorTag> predicate = new SafePredicate<EclipseEditorTag>(){
							@Override
							public boolean eval(EclipseEditorTag editorTag) {
								return editorTag.includes(offset) && 
								(	(editorTag.getElement() instanceof Type) 
										|| (editorTag.getElement() instanceof TypeReference)
								);
							}
						};
						Collection<EclipseEditorTag> tags = new TreeSet<EclipseEditorTag>(EclipseEditorTag.lengthComparator);
						doc.getEditorTagsWithPredicate(predicate, tags);
						if(tags != null && tags.size()>0){
							for(EclipseEditorTag tag : tags ){
								if(tag!=null && tag.getElement() instanceof Type){
									return (Type)tag.getElement();
								} else if(tag!=null && tag.getElement() instanceof TypeReference){
									return ((TypeReference)tag.getElement()).getElement();
								}
							}
						}
					}
				}
				// if no type(reference) surrounding selection found, get top type of compilation unit
				return chamEditor.getDocument().document().descendants(Type.class).iterator().next();
			}
		} catch (ModelException e) {
//			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Open the doubleclicked element (in new editor if necessary) when doubleclicked on it
	 * 
	 * This method is used in both, the typeviewer and the memberviewer.
	 */
	@Override
   public void doubleClick(DoubleClickEvent event) {
		if(event.getSelection() instanceof IStructuredSelection) {
			IStructuredSelection selection = (IStructuredSelection)event.getSelection();
			Object firstObject = selection.getFirstElement();
			if(firstObject instanceof HierarchyTypeNode){
				try {
					Type type = ((HierarchyTypeNode)firstObject).getType();
					ChameleonEditor.showInEditor(type, true, true, getEditor());
				} catch (ModelException e) {
					e.printStackTrace();
				}
				//new TreeViewerActions.RefreshAction(view.getHierarchyViewer()).run();
			} else if(firstObject instanceof Element){
				ChameleonEditor.showInEditor((Element)firstObject, true, true, getEditor());
			}
			new TypeChangedListener(view).selectionChanged(view.getHierarchyViewer().getSelection());//TOTEST
			
		} else if(event.getSelection().isEmpty()){
			getEditor().resetHighlightRange();
		}
	}

	/**
	 * Open an element (if already opened in editor) when clicked on it
	 * 
	 * This method is used in both, the typeviewer and the memberviewer.
	 */
	@Override
   public void selectionChanged(SelectionChangedEvent event) {
		if(event.getSelection() instanceof IStructuredSelection) {
			IStructuredSelection selection = (IStructuredSelection)event.getSelection();
			Object firstObject = selection.getFirstElement();
			if(firstObject instanceof HierarchyTypeNode){
				try {
					Type type = ((HierarchyTypeNode)firstObject).getType();
					ChameleonEditor.showInEditor(type, false, false, getEditor());
				} catch (ModelException e) {
					e.printStackTrace();
				}
			} else if(firstObject instanceof Element){
				ChameleonEditor.showInEditor((Element)firstObject, false, false, getEditor());
			}
		} else if(event.getSelection().isEmpty()){
			getEditor().resetHighlightRange();
		}
	}

	/**
	 * remember the CHAMELEON EDITOR
	 */

	private ChameleonEditor editor;

	protected ChameleonEditor getEditor(){
		if(editor==null)
			editor = ChameleonEditor.getActiveEditor();
		return editor;
	}

	protected ChameleonEditor getCurrentEditor(){
		ChameleonEditor currEditor = ChameleonEditor.getActiveEditor();
		if(currEditor != null){
			editor = currEditor;
		}
		return editor;
	}

	protected void setEditor(ChameleonEditor editor){
		this.editor = editor;
	}


}
