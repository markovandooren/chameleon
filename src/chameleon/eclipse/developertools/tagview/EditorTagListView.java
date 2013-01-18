/**
 * Created on 7-mei-07
 * @author Tim Vermeiren
 */
package chameleon.eclipse.developertools.tagview;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.part.ViewPart;
import org.rejuse.predicate.Predicate;
import org.rejuse.predicate.True;

import chameleon.eclipse.ChameleonEditorPlugin;
import chameleon.eclipse.connector.EclipseEditorTag;
import chameleon.eclipse.editors.EclipseDocument;
import chameleon.eclipse.editors.ChameleonEditor;
import chameleon.eclipse.presentation.treeview.ChameleonLabelProvider;
import chameleon.input.PositionMetadata;

/**
 * Shows the Editor Tags of the current Chameleon Document.
 * 
 * @author Tim Vermeiren
 */
public class EditorTagListView extends ViewPart {
  //FIXME: does not correctly highlight multiple positions with the same name (keyword) for a single element
	//FIXME: does not show positions if their range is the same.
	
	TableViewer viewer;
	Label label;
	ChameleonEditor editor;

	@Override
	public void createPartControl(Composite parent) {
		// lay out the grid of the view
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.verticalSpacing = 2;
		layout.marginWidth = 0;
		layout.marginHeight = 2;
		parent.setLayout(layout);

		// add label
		label = new Label(parent, SWT.VERTICAL | SWT.SHADOW_IN | SWT.LEFT );
		label.setText("Editor Tag List View");
		// layout the label above the TableViewer
		GridData layoutData = new GridData();
		layoutData.grabExcessHorizontalSpace = true;
		layoutData.horizontalAlignment = GridData.FILL;
		label.setLayoutData(layoutData);

		// add TableViewer
		viewer = new TableViewer(parent, SWT.VERTICAL | SWT.BORDER);
		viewer.setContentProvider(new EditorTagContentProvider(this , new True<EclipseEditorTag>()));
		viewer.addSelectionChangedListener(new EditorTagSelectionChangedListener());
		viewer.addDoubleClickListener(new EditorTagDoubleClickListener());
		// lay out the TableViewer
		layoutData = new GridData();
		layoutData.grabExcessHorizontalSpace = true;
		layoutData.grabExcessVerticalSpace = true;
		layoutData.horizontalAlignment = GridData.FILL;
		layoutData.verticalAlignment = GridData.FILL;
		viewer.getControl().setLayoutData(layoutData);

		// create actions:
		Action showTagsAction = new ShowEditorTagListAction();
		Action showAllTagsAction = new ShowAllEditorTagListAction();
		Action showTagsAtPosAction = new ShowEditorTagListAtPositionAction();
		Action clearAction = new ClearAction();

		// create menu:
		IContributionManager mgr = getViewSite().getActionBars().getMenuManager();
		mgr.add(showTagsAction);
		mgr.add(showAllTagsAction);
		mgr.add(showTagsAtPosAction);
		mgr.add(clearAction);

		// create toolbar:
		mgr = getViewSite().getActionBars().getToolBarManager();
		mgr.add(showTagsAction);
		mgr.add(showAllTagsAction);
		mgr.add(showTagsAtPosAction);
		mgr.add(clearAction);

	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
		label.setFocus();
	}

	private class PredicateShowEditorTagListAction extends Action {
		private Predicate<EclipseEditorTag> _predicate;
		
		public PredicateShowEditorTagListAction(String message, Predicate<EclipseEditorTag> predicate) {
			super(message);
			_predicate = predicate;
		}

		@Override
		public void run() {
			editor = ChameleonEditor.getActiveEditor();
			if(editor != null){
				EclipseDocument doc = editor.getDocument();
				// set filter predicate to True
				((EditorTagContentProvider)viewer.getContentProvider()).setFilter(_predicate);
				viewer.setLabelProvider(new ChameleonLabelProvider(doc.language(), false, false, true));
				viewer.setInput(doc);
			} else {
				ChameleonEditorPlugin.showMessageBox("Error", "Error while trying to open Editor Tag List View.\nNo Chameleon editor found.\nThe current active editor must be an Chameleon Editor.", SWT.ICON_ERROR);
			}
		}
}

	private class ShowEditorTagListAction extends PredicateShowEditorTagListAction {
		public ShowEditorTagListAction() {
			super("Show Editor Tags",new True<EclipseEditorTag>());
		}
	}

	private class ShowAllEditorTagListAction extends PredicateShowEditorTagListAction {
		public ShowAllEditorTagListAction() {
			super("Show ALL-Editor Tags",new EclipseEditorTag.NamePredicate(PositionMetadata.ALL));
		}
	}

	private class ShowEditorTagListAtPositionAction extends Action {
		public ShowEditorTagListAtPositionAction() {
			super("Show Editor Tags round cursor");
		}
		@Override
		public void run() {
			try {
				editor = ChameleonEditor.getActiveEditor(); // can be null
				if(editor!=null){
				EclipseDocument doc = editor.getDocument(); // can throw NullPointerException
				int offset = ((TextSelection)editor.getSelectionProvider().getSelection()).getOffset(); // can throw ClassCastException or NullPointerException
				// set filter predicate to "only all-editor tags"
				((EditorTagContentProvider)viewer.getContentProvider()).setFilter(new EclipseEditorTag.SurroundsOffsetPredicate(offset));
				viewer.setLabelProvider(new ChameleonLabelProvider(doc.language(), false, false, false));
				viewer.setInput(doc);
				} else {
					ChameleonEditorPlugin.showMessageBox("Error", "Error while trying to open Editor Tag List View.\nNo Chameleon editor found.\nThe current active editor must be an Chameleon Editor.", SWT.ICON_ERROR);
				}
			} catch (RuntimeException e) {
				// ClassCastException or NullPointerException may be thrown
				ChameleonEditorPlugin.showMessageBox("Error", "Error while trying to open Editor Tag List View.\n"+e.getMessage(), SWT.ICON_ERROR);
				e.printStackTrace();
			}
		}

	}

	private class ClearAction extends Action {
		public ClearAction() {
			super("Clear view");
			setImageDescriptor(ChameleonEditorPlugin.getImageDescriptor("delete.gif"));
		}

		@Override
		public void run() {
			viewer.setInput(null);
			label.setText("Editor Tag List View");
		}
	}

	private class EditorTagSelectionChangedListener implements ISelectionChangedListener {
		public void selectionChanged(SelectionChangedEvent event) {
			ISelection sel = event.getSelection();
			if(sel instanceof StructuredSelection){
				Object selectedObject = ((StructuredSelection)sel).getFirstElement();
				if(selectedObject instanceof EclipseEditorTag){
					EclipseEditorTag tag = ((EclipseEditorTag) selectedObject);
					// bring editor to top:
					IWorkbenchPage page = Workbench.getInstance().getActiveWorkbenchWindow().getActivePage();
					page.bringToTop(editor);
					// hightlight element
//					editor.highLightElement(tag.getElement(), true, tag.getName());
					editor.highlight(tag, true);
				}
			}
		}
	}

	private class EditorTagDoubleClickListener implements IDoubleClickListener {
		public void doubleClick(DoubleClickEvent event) {
			ISelection sel = event.getSelection();
			if(sel instanceof StructuredSelection){
				Object selectedObject = ((StructuredSelection)sel).getFirstElement();
				if(selectedObject instanceof EclipseEditorTag){
					EclipseEditorTag tag = ((EclipseEditorTag) selectedObject);
					editor.highlight(tag, true);
					// FIXME: why is this static? and why a different invocation than in the selection changed listener?
//					ChameleonEditor.showInEditor(tag.getElement(), true, editor, true, tag.getName());
				}
			}
		}
	}
}
