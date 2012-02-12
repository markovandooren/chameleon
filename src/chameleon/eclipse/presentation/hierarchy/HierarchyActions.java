/**
 * Created on 25-apr-07
 * @author Tim Vermeiren
 */
package chameleon.eclipse.presentation.hierarchy;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;

import chameleon.eclipse.ChameleonEditorPlugin;
import chameleon.eclipse.presentation.treeview.ChameleonLabelProvider;

/**
 * This class contains some small simple actions for the HierarchyView
 * 
 * @author Tim Vermeiren
 */
public class HierarchyActions {

	/**
	 * Shows also inherited members
	 */
	public static class ShowInheritedMembersAction extends Action {
		private StructuredViewer memberViewer;
		// constructor:
		public ShowInheritedMembersAction(StructuredViewer memberViewer) {
			super("Show all inherited members", AS_CHECK_BOX);
			this.memberViewer = memberViewer;
			setImageDescriptor(ChameleonEditorPlugin.getImageDescriptor("show_inherited_members.gif"));
			if(memberViewer.getContentProvider() instanceof AllMemberContentProvider)
				setChecked(true);
		}
		@Override
		public void run() {
			ChameleonLabelProvider currentLabelProvider = (ChameleonLabelProvider)memberViewer.getLabelProvider();
			IContentProvider currentContentProvider = memberViewer.getContentProvider();
			// if only direct members shown:
			if(currentContentProvider instanceof MemberContentProvider){
				// and show the fqn's of the declaring types
				currentLabelProvider.setShowDeclaringElementFqn(true);
				// show all inherited members
				memberViewer.setContentProvider(new AllMemberContentProvider());
				// enable this button
				setChecked(true);
			} else {
				// and hide the fqn's of the declaring types
				currentLabelProvider.setShowDeclaringElementFqn(false);
				// if inherited members shown, show only direct members
				memberViewer.setContentProvider(new MemberContentProvider());
				// disable this button
				setChecked(false);
			}  
		}
	}

	public static class ViewVerticalAction extends Action {
		private SashForm sashForm;
		public ViewVerticalAction(SashForm sashForm){
			super("Vertical View", AS_RADIO_BUTTON);
			setImageDescriptor(ChameleonEditorPlugin.getImageDescriptor("th_vertical.gif"));
			this.sashForm = sashForm;
		}
		@Override
		public void run() {
			sashForm.setOrientation(SWT.VERTICAL);
			sashForm.setWeights(new int[]{50,50});
		}
	}

	public static class ViewHorizontalAction extends Action {
		private SashForm sashForm;
		public ViewHorizontalAction(SashForm sashForm){
			super("Horizontal View", AS_RADIO_BUTTON);
			setImageDescriptor(ChameleonEditorPlugin.getImageDescriptor("th_horizontal.gif"));
			this.sashForm = sashForm;
		}
		@Override
		public void run() {
			sashForm.setOrientation(SWT.HORIZONTAL);
			sashForm.setWeights(new int[]{50,50});
		}
	}

	public static class ViewOnlyHierarchyAction extends Action {
		private SashForm sashForm;
		private int[] weights = new int[]{1000,0};
		public ViewOnlyHierarchyAction(SashForm sashForm){
			super("Show only Hierarchy view", AS_RADIO_BUTTON);
			setImageDescriptor(ChameleonEditorPlugin.getImageDescriptor("th_single.gif"));
			this.sashForm = sashForm;
		}
		@Override
		public void run() {
			sashForm.setWeights(weights);
		}
	}

	public static class RefreshAction extends Action{
		private HierarchyView hierarchyView;
		public RefreshAction(HierarchyView hierarchyView){
			super("Refresh");
			setImageDescriptor(ChameleonEditorPlugin.getImageDescriptor("refresh.gif"));
			this.hierarchyView = hierarchyView;
		}
		
		/**
		 * Will refresh the hierarchyViewer and the MemberViewer.
		 * This will cause to collapse all types in the HierarchyViewer, because
		 * the HierarchyContentProvider returns new HierarchyTypeNode objects each
		 * time the getChildren() method is called.
		 */
		@Override
		public void run() {
			TreeViewer hierarchyViewer = hierarchyView.getHierarchyViewer();
			StructuredViewer memberViewer = hierarchyView.getMemberViewer();
			// save selection:
			ISelection sel = hierarchyViewer.getSelection();
			// refresh the hierarchyviewer
			Object currentInput = hierarchyViewer.getInput();
			hierarchyViewer.setInput(currentInput);
			hierarchyViewer.refresh();
			hierarchyViewer.expandAll();
			// set selection:
			new TypeChangedListener(hierarchyView).selectionChanged(sel);
			// refresh the memberViewer
			memberViewer.setInput(memberViewer.getInput());
			memberViewer.refresh();
		}
	}



}
