/**
 * Created on 13-jun-07
 * @author Tim Vermeiren
 */
package chameleon.eclipse.presentation.callhierarchy;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import chameleon.eclipse.presentation.treeview.TreeViewerActions;

/**
 * This view will shows all references to the current declaration
 * or all the declarations referenced by the current declaration.
 * 
 * @author Marko van Dooren
 * @author Tim Vermeiren
 */
public class CallHierarchyView extends ViewPart {
	
	private TreeViewer _treeViewer;

	TreeViewer getTreeViewer(){
		return _treeViewer;
	}
	
	/**
	 * Initialize this View
	 */
	@Override
	public void createPartControl(Composite parent) {
		_treeViewer = new TreeViewer(parent);
		_treeViewer.setAutoExpandLevel(2);
		createActions();
	}
	
	private OpenCallersHierarchyAction _openCallersHierarchyAction;
	private OpenCalleesHierarchyAction _openCalleesHierarchyAction;
	private MethodClickedListener _clickedListener;
	
	private void createActions(){
		// create open call hierarchy action:
		_openCallersHierarchyAction = new OpenCallersHierarchyAction(this);
		_openCalleesHierarchyAction = new OpenCalleesHierarchyAction(this);
		// add listeners:
		_clickedListener = new MethodClickedListener();
		_treeViewer.addSelectionChangedListener(_clickedListener);
		_treeViewer.addDoubleClickListener(_clickedListener);
		// add actions to menu:
		createToolbar();
		createMenu();
	}
	
	/**
	 * Creates the toolbar (adds the actions to the toolbarmanager)
	 */
	private void createToolbar() {
		IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
		mgr.add(_openCallersHierarchyAction);
		mgr.add(_openCalleesHierarchyAction);
		mgr.add(new Separator("Treeviewer actions"));
		mgr.add(new TreeViewerActions.RefreshAction(_treeViewer));
		mgr.add(new TreeViewerActions.CollapseAllAction(_treeViewer));
		mgr.add(new TreeViewerActions.ClearViewerAction(_treeViewer));
	}
	
	/**
	 * Creates the menu (adds the actions to the menu manager)
	 */
	private void createMenu() {
		IMenuManager mgr = getViewSite().getActionBars().getMenuManager();
		mgr.add(_openCallersHierarchyAction);
		mgr.add(_openCalleesHierarchyAction);
		mgr.add(new Separator("Treeviewer actions"));
		mgr.add(new TreeViewerActions.RefreshAction(_treeViewer));
		mgr.add(new TreeViewerActions.CollapseAllAction(_treeViewer));
		mgr.add(new TreeViewerActions.ClearViewerAction(_treeViewer));
	}
	
	@Override
	public void setFocus() {
		_treeViewer.getControl().setFocus();
	}
	
}

