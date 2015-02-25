/**
 * Created on 30-apr-07
 * @author Tim Vermeiren
 */
package org.aikodi.chameleon.eclipse.presentation.treeview;

import org.aikodi.chameleon.eclipse.ChameleonEditorPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.text.Assert;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

/**
 * Actions that can be used for different treeViewers 
 * e.g. the outline, the type hierarhcy
 * 
 * @author Tim Vermeiren
 */
public class TreeViewerActions {

	/**
	 * Adds the standard treeviewer actions for the given viewer to the given ContributorManager
	 * 
	 * @param mgr		this might be the menu- or toolbarmanager
	 * @param viewer	the treeviewer the actions has to apply on
	 */
	public static void addStandardTreeViewerActions(IContributionManager mgr, TreeViewer viewer){
		mgr.add(new Separator("TreeViewer actions"));
		mgr.add(new CollapseAllAction(viewer));
		mgr.add(new ExpandAllAction(viewer));
		mgr.add(new RefreshAction(viewer));
		mgr.add(new ClearViewerAction(viewer));
	}

	public static void addExpandToLevelSubmenu(int nbLevels, IContributionManager mgr, TreeViewer viewer){
		Assert.isTrue(nbLevels>=2);
		IMenuManager submenu = new MenuManager("Expand to level...");
		for(int i=2; i<=nbLevels; i++){
			submenu.add(new TreeViewerActions.ExpandToLevelAction(i, viewer));
		}
		mgr.add(submenu);
	}

	public static class RefreshAction extends Action{
		private Viewer[] viewers;
		public RefreshAction(Viewer... viewers){
			super("Refresh");
			setImageDescriptor(ChameleonEditorPlugin.getImageDescriptor("refresh.gif"));
			this.viewers = viewers;
		}
		@Override
		public void run() {
			for(Viewer viewer : viewers){
				viewer.setInput(viewer.getInput());
				viewer.refresh();
			}
		}
	}

	public static class CollapseAllAction extends Action {
		private TreeViewer treeViewer;
		// constructor:
		public CollapseAllAction(TreeViewer viewer) {
			super("Collapse All");
			setImageDescriptor(ChameleonEditorPlugin.getImageDescriptor("collapseall.gif"));
			this.treeViewer = viewer;
		}
		@Override
		public void run() {
			treeViewer.setAutoExpandLevel(0);
			treeViewer.collapseAll();
		}
	}

	public static class ExpandAllAction extends Action {
		private TreeViewer treeViewer;
		// constructor:
		public ExpandAllAction(TreeViewer treeViewer) {
			super("Expand All");
			setImageDescriptor(ChameleonEditorPlugin.getImageDescriptor("expandall.gif"));
			this.treeViewer = treeViewer;
		}
		@Override
		public void run() {
			treeViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);
			treeViewer.expandAll();
		}
	}

	public static class ExpandToLevelAction extends Action {
		private TreeViewer treeViewer;
		private int level;
		// constructor:
		public ExpandToLevelAction(int level, TreeViewer treeViewer) {
			super("Expand to level "+level);
			setImageDescriptor(ChameleonEditorPlugin.getImageDescriptor("hierarchicalLayout.gif"));
			this.treeViewer = treeViewer;
			this.level = level;
		}
		@Override
		public void run() {
			treeViewer.collapseAll();
			treeViewer.setAutoExpandLevel(level);
			treeViewer.expandToLevel(level);
		}
	}

	public static class ClearViewerAction extends Action {
		private StructuredViewer[] viewers;
		// constructor:
		public ClearViewerAction(StructuredViewer... viewers) {
			super("Clear All");
			setImageDescriptor(ChameleonEditorPlugin.getImageDescriptor("delete.gif"));
			this.viewers = viewers;
		}
		@Override
		public void run() {
			for(StructuredViewer viewer : viewers){
				viewer.setInput(null);
			}
		}
	}

}
