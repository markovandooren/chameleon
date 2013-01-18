/**
 * Created on 2-mei-07
 * @author Tim Vermeiren
 */
package chameleon.eclipse.developertools.modelview;

import java.util.Collection;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;

import chameleon.core.element.Element;
import chameleon.core.language.Language;
import chameleon.core.tag.Metadata;
import chameleon.eclipse.connector.EclipseEditorTag;
import chameleon.eclipse.editors.ChameleonEditor;
import chameleon.eclipse.presentation.treeview.ChameleonLabelProvider;
import chameleon.eclipse.presentation.treeview.TreeViewerActions;
import chameleon.input.PositionMetadata;

/**
 * This class contains a view intended to be used by Chameleon programmers. 
 * So, for debugging purposes of Chameleon.
 * You can open this view in the Chameleon editor by chosing:
 * Window -> Show view -> Other... -> Chameleon Metamodel
 * 
 * 
 * @author Tim Vermeiren
 */
public class ModelView extends ViewPart {

	private TreeViewer modelViewer;
	
	private DrillDownAdapter drillDownAdapter;
	
	/**
	 * Creates the Chameleon Metamodel view in Eclipse.
	 */
	@Override
	public void createPartControl(Composite parent) {
		modelViewer = new TreeViewer(parent);
		modelViewer.setContentProvider(new ModelContentProvider());
		drillDownAdapter = new DrillDownAdapter(modelViewer);
		
		// create menu:
		createMenu();
		// create context menu
		createContextMenu();
		// add listeners:
		modelViewer.addSelectionChangedListener(new ModelViewSelectionChangedListener());
		modelViewer.addDoubleClickListener(new ModelViewDoubleClickListener());
	}

	private void createMenu() {
		// create menu:
		IMenuManager menuMgr = getViewSite().getActionBars().getMenuManager();
		addModelviewActions(menuMgr);
		menuMgr.add(new Separator("Drilldown menu"));
		drillDownAdapter.addNavigationActions(menuMgr);
		TreeViewerActions.addStandardTreeViewerActions(menuMgr, modelViewer);
		TreeViewerActions.addExpandToLevelSubmenu(10, menuMgr, modelViewer);
		menuMgr.add(new Separator());
		// create toolbar:
		IToolBarManager toolMgr = getViewSite().getActionBars().getToolBarManager();
		addModelviewActions(toolMgr);
		toolMgr.add(new Separator("Drilldown menu"));
		drillDownAdapter.addNavigationActions(toolMgr);
		TreeViewerActions.addStandardTreeViewerActions(toolMgr, modelViewer);
		toolMgr.add(new Separator());

	}

	private void createContextMenu(){
		MenuManager mgr = new MenuManager();
		mgr.setRemoveAllWhenShown(true);
		mgr.addMenuListener(new IMenuListener(){
			public void menuAboutToShow(IMenuManager manager) {
				fillContextMenu(manager);
			}
		});

		// create Menu:
		Menu menu = mgr.createContextMenu(modelViewer.getControl());
		modelViewer.getControl().setMenu(menu);

		// Register menu for extension (other plugins might add items to this context menu!)
		getSite().registerContextMenu(mgr, modelViewer);
	}

	protected void fillContextMenu(IMenuManager manager) {
		addShowEditorTagsSubmenu(manager);
		manager.add(new Separator());
		manager.add(new ShowDocumentModelAction());
		manager.add(new Separator("Drilldown menu"));
		drillDownAdapter.addNavigationActions(manager);
		manager.add(new ShowWholeModelAction());
		manager.add(new Separator());
		TreeViewerActions.addStandardTreeViewerActions(manager, modelViewer);
		TreeViewerActions.addExpandToLevelSubmenu(10, manager, modelViewer);

	}

	private void addModelviewActions(IContributionManager mgr){
		mgr.add(new ShowDocumentModelAction());
		mgr.add(new ShowWholeModelAction());

	}

	/**
	 * Shows only the metamodel of the currently opened document
	 * 
	 * @author Tim Vermeiren
	 */
	private class ShowDocumentModelAction extends Action {
		public ShowDocumentModelAction() {
			super("Show Document Model");
		}
		@Override
		public void run() {
			ChameleonEditor editor = ChameleonEditor.getActiveEditor();
			if(editor != null){
				Language lang = editor.getDocument().document().language();
				drillDownAdapter.reset();
				modelViewer.setLabelProvider(new ChameleonLabelProvider(lang, true, false, true));
				modelViewer.setInput(editor.getDocument().document());
			}
		}
	}

	/**
	 * Show the whole metamodel of Chameleon of the current project.
	 * 
	 * @author Tim Vermeiren
	 */
	private class ShowWholeModelAction extends Action {
		public ShowWholeModelAction() {
			super("Show Whole Metamodel");
		}
		@Override
		public void run() {
			ChameleonEditor editor = ChameleonEditor.getActiveEditor();
			if(editor != null){
				Language lang = editor.getDocument().document().language();
				drillDownAdapter.reset();
				modelViewer.setLabelProvider(new ChameleonLabelProvider(lang, true, false, true));
				modelViewer.setInput(editor.getDocument().getProjectNature().getModel());
			}
		}
	}

	private void addShowEditorTagsSubmenu(IContributionManager menuMgr){
		ISelection sel = modelViewer.getSelection();
		if(sel instanceof StructuredSelection){
			Object firstObject = ((StructuredSelection)sel).getFirstElement();
			if(firstObject instanceof Element){
				Element element = (Element) firstObject;
				IMenuManager subMenuMgr = new MenuManager("Show editor tag in editor");
				Collection<Metadata> tags = element.metadata();
				for(Metadata currentTag : tags){
					if(currentTag instanceof EclipseEditorTag){
						subMenuMgr.add(new ShowEditorTagAction(modelViewer, (EclipseEditorTag)currentTag));
					}
				}
				// Add submenu to menu:
				menuMgr.add(subMenuMgr);
			}
		}
	}

	/**
	 * Shows the selected element in the (currently opened) editor.
	 * 
	 * @author Tim Vermeiren
	 */
	private class ShowEditorTagAction extends Action {
		private TreeViewer modelViewer;
		private EclipseEditorTag editorTag;
		public ShowEditorTagAction(TreeViewer modelViewer, EclipseEditorTag editorTag) {
			super(editorTag.getName() + " (" + editorTag.getOffset() + " - " + (editorTag.getOffset()+editorTag.getLength()) + ")");
			this.modelViewer = modelViewer;
			this.editorTag = editorTag;
		}
		@Override
		public void run() {
			ISelection sel = modelViewer.getSelection();
			if(sel instanceof StructuredSelection){
				Object selectedObject = ((StructuredSelection)sel).getFirstElement();
				if(selectedObject instanceof Element){
					Element element = ((Element) selectedObject);
					ChameleonEditor.showInEditor(element, false, false, null, true, editorTag.getName());
				}
			}
		}
	}


	/**
	 * Shows the selected element in the (currently opened) editor.
	 * 
	 * @author Tim Vermeiren
	 */
	private class ModelViewSelectionChangedListener implements ISelectionChangedListener {
		public void selectionChanged(SelectionChangedEvent event) {
			ISelection sel = event.getSelection();
			if(sel instanceof StructuredSelection){
				Object selectedObject = ((StructuredSelection)sel).getFirstElement();
				if(selectedObject instanceof Element){
					Element element = ((Element) selectedObject);
					ChameleonEditor editor = ChameleonEditor.getActiveEditor();
					boolean succeeded = ChameleonEditor.showInEditor(element, false, false, editor, true, PositionMetadata.ALL);
					// if no document of element found, just highlight the EditorTag in the current active editor:
					// eg. Modifiers cannot be highlighted with showInEditor
					if(!succeeded && editor != null){
						editor.highLightElement(element);
					}
				}
			}
		}
	}

	/**
	 * Shows the selected element in the editor and will open a new editor if necessary.
	 * 
	 * @author Tim Vermeiren
	 */
	private class ModelViewDoubleClickListener implements IDoubleClickListener {
		public void doubleClick(DoubleClickEvent event) {
			ISelection sel = event.getSelection();
			if(sel instanceof StructuredSelection){
				Object selectedObject = ((StructuredSelection)sel).getFirstElement();
				if(selectedObject instanceof Element){
					Element element = ((Element) selectedObject);
					ChameleonEditor.showInEditor(element, true, true, null, true, PositionMetadata.ALL);
				}
			}
		}
	}



	@Override
	public void setFocus() {
		modelViewer.getControl().setFocus();
	}

}
