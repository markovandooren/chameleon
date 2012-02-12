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
 * This View will show all the methods invocating the current method
 * or all the methods the current method calls.
 * 
 * @author Tim Vermeiren
 */
public class CallHierarchyView extends ViewPart {
	
	private TreeViewer treeViewer;

	TreeViewer getTreeViewer(){
		return treeViewer;
	}
	
	/**
	 * Initialize this View
	 */
	@Override
	public void createPartControl(Composite parent) {
		treeViewer = new TreeViewer(parent);
		treeViewer.setAutoExpandLevel(2);
		createActions();
	}
	
	private OpenCallersHierarchyAction openCallersHierarchyAction;
	private OpenCalleesHierarchyAction openCalleesHierarchyAction;
	private MethodClickedListener methodClickedListener;
	
	private void createActions(){
		// create open call hierarchy action:
		openCallersHierarchyAction = new OpenCallersHierarchyAction(this);
		openCalleesHierarchyAction = new OpenCalleesHierarchyAction(this);
		// add listeners:
		methodClickedListener = new MethodClickedListener();
		treeViewer.addSelectionChangedListener(methodClickedListener);
		treeViewer.addDoubleClickListener(methodClickedListener);
		// add actions to menu:
		createToolbar();
		createMenu();
	}
	
	/**
	 * Creates the toolbar (adds the actions to the toolbarmanager)
	 */
	private void createToolbar() {
		IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
		mgr.add(openCallersHierarchyAction);
		mgr.add(openCalleesHierarchyAction);
		mgr.add(new Separator("Treeviewer actions"));
		mgr.add(new TreeViewerActions.RefreshAction(treeViewer));
		mgr.add(new TreeViewerActions.CollapseAllAction(treeViewer));
		mgr.add(new TreeViewerActions.ClearViewerAction(treeViewer));
		
	}
	
	/**
	 * Creates the menu (adds the actions to the menu manager)
	 */
	private void createMenu() {
		IMenuManager mgr = getViewSite().getActionBars().getMenuManager();
		mgr.add(openCallersHierarchyAction);
		mgr.add(openCalleesHierarchyAction);
		mgr.add(new Separator("Treeviewer actions"));
		mgr.add(new TreeViewerActions.RefreshAction(treeViewer));
		mgr.add(new TreeViewerActions.CollapseAllAction(treeViewer));
		mgr.add(new TreeViewerActions.ClearViewerAction(treeViewer));
		
	}
	
	@Override
	public void setFocus() {
		treeViewer.getControl().setFocus();
	}

	
}

