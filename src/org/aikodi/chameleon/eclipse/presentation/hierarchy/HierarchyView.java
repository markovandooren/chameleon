/**
 * Created on 20-apr-07
 * @author Tim Vermeiren
 */
package org.aikodi.chameleon.eclipse.presentation.hierarchy;

import static org.aikodi.chameleon.eclipse.presentation.hierarchy.OpenTypeHierarchyAction.SUBTYPE;
import static org.aikodi.chameleon.eclipse.presentation.hierarchy.OpenTypeHierarchyAction.SUPERTYPE;

import org.aikodi.chameleon.eclipse.editors.actions.IChameleonEditorActionDefinitionIds;
import org.aikodi.chameleon.eclipse.presentation.Filters;
import org.aikodi.chameleon.eclipse.presentation.treeview.ChameleonViewComparator;
import org.aikodi.chameleon.eclipse.presentation.treeview.TreeViewerActions;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.IViewDescriptor;

/**
 * The Chameleon hierarchy type view. 
 * This view is used to show the type hierarchy and the members of a certain type.
 * 
 * This view is added to eclipse by the plugin.xml file.
 * 
 * @author Tim Vermeiren
 */
public class HierarchyView extends ViewPart {
	
	/**
	 * The label showing the name of type which type hierarchy is currently opened
	 */
	private Label label;
	
	/**
	 * Container for the hierarchyViewer and the memberviewer
	 * so they are both resizable
	 */
	private SashForm sashForm;
	
	/**
	 * The viewer showing the tree structure of the type hierarchy
	 */
	private TreeViewer hierarchyViewer;
	
	/**
	 * The viewer to show the members of the selected type in de type hierarchy
	 */
	private TableViewer memberViewer;
	
	TreeViewer getHierarchyViewer(){
		return hierarchyViewer;
	}

	Label getLabel(){
		return label;
	}

	StructuredViewer getMemberViewer(){
		return memberViewer;
	}

	@Override
   public void setFocus() {
		hierarchyViewer.getControl().setFocus();
	}
	
	/**
	 * Creates this view, adds the viewers to this view, 
	 * adds actions to the menu and the toolbar
	 */
	@Override
   public void createPartControl(Composite parent) {
		/* Create a grid layout object so the text and treeviewer
		 * are layed out the way I want. */
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.verticalSpacing = 2;
		layout.marginWidth = 0;
		layout.marginHeight = 2;
		parent.setLayout(layout);

		// Create the label above the treeviewer
		label = new Label(parent, SWT.LEFT | SWT.SHADOW_IN | SWT.VERTICAL);
		label.setText("Type Hierarchy");
		//	layout the label above the treeviewer
		GridData layoutData = new GridData();
		layoutData.grabExcessHorizontalSpace = true;
		layoutData.horizontalAlignment = GridData.FILL;
		label.setLayoutData(layoutData);

		// create SashForm that will contain the hierarchyviewer and the memberviewer, so they are resizable
		sashForm = new SashForm(parent, SWT.VERTICAL);
		// layout the sashForm below the text field
		layoutData = new GridData();
		layoutData.grabExcessHorizontalSpace = true;
		layoutData.grabExcessVerticalSpace = true;
		layoutData.horizontalAlignment = GridData.FILL;
		layoutData.verticalAlignment = GridData.FILL;
		sashForm.setLayoutData(layoutData);

		// create treeViewer:
		hierarchyViewer = new TreeViewer(sashForm);
		hierarchyViewer.setContentProvider(new SuperTypeHierarchyContentProvider());
		hierarchyViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);
		
		// layout the tree viewer below the text field
		layoutData = new GridData();
		layoutData.grabExcessHorizontalSpace = true;
		layoutData.grabExcessVerticalSpace = true;
		layoutData.horizontalAlignment = GridData.FILL;
		layoutData.verticalAlignment = GridData.FILL;
		hierarchyViewer.getControl().setLayoutData(layoutData);
		
//		ChameleonEditor editor = ChameleonEditor.getCurrentActiveEditor();
//		Language language = editor.getDocument().language();
//		DeclarationCategorizer categorizer = language.connector(EclipseEditorExtension.class).declarationCategorizer();
		// create the member table viewer
		memberViewer = new TableViewer(sashForm);
		memberViewer.setContentProvider(new MemberContentProvider());
		memberViewer.setComparator(new ChameleonViewComparator());
		// layout the tree viewer below the text field
		layoutData = new GridData();
		layoutData.grabExcessHorizontalSpace = true;
		layoutData.grabExcessVerticalSpace = true;
		layoutData.horizontalAlignment = GridData.FILL;
		layoutData.verticalAlignment = GridData.FILL;
		memberViewer.getControl().setLayoutData(layoutData);
		// create actions, menu and toolbar:
		createActions();

	}
	
	protected OpenTypeHierarchyAction subHierarchyAction;
	
	protected OpenTypeHierarchyAction superHierarchyAction;
	
	/**
	 * Creates the actions for the type hierarchy and adds them to
	 * the menu and the toolbar.
	 * Adds the listeners as well.
	 */
	private void createActions(){
		// create actions: 
		subHierarchyAction = new OpenTypeHierarchyAction(SUBTYPE, this);
		subHierarchyAction.setActionDefinitionId(IChameleonEditorActionDefinitionIds.SUB_HIERARCHY);
		superHierarchyAction = new OpenTypeHierarchyAction(SUPERTYPE, this);
		superHierarchyAction.setActionDefinitionId(IChameleonEditorActionDefinitionIds.SUPER_HIERARCHY);
		TypeChangedListener typeChangedListener = new TypeChangedListener(this);

		// add listeners to memberviewer (avoid dubble use of same listener instance)
		memberViewer.addSelectionChangedListener(subHierarchyAction);
		subHierarchyAction = new OpenTypeHierarchyAction(SUBTYPE, this);
		memberViewer.addDoubleClickListener(subHierarchyAction);
		// add listeners to hierarchyViewer
		subHierarchyAction = new OpenTypeHierarchyAction(SUBTYPE, this);
		hierarchyViewer.addSelectionChangedListener(subHierarchyAction);
		subHierarchyAction = new OpenTypeHierarchyAction(SUBTYPE, this);
		hierarchyViewer.addDoubleClickListener(subHierarchyAction);
		
		// listener to show the members when clicked on a type:
		hierarchyViewer.addSelectionChangedListener(typeChangedListener);
		
		// Create menu:
		createMenu();
		
		// Create toolbar:
		createToolbar();
		
		// create context menu 
		createHierarchyContextMenu();
		createMemberContextMenu();
		
		// update action bars:
		getViewSite().getActionBars().updateActionBars();
	}

	private void createHierarchyContextMenu() {
		MenuManager mgr = new MenuManager();
		mgr.setRemoveAllWhenShown(true);
		mgr.addMenuListener(new IMenuListener(){
			@Override
         public void menuAboutToShow(IMenuManager manager) {
				fillHierarchyContextMenu(manager);
			}
		});
		Menu menu = mgr.createContextMenu(hierarchyViewer.getControl());
		hierarchyViewer.getControl().setMenu(menu);

		// Register context menu for extension (other plugins might add items to this context menu!)
		//getSite().registerContextMenu(mgr, hierarchyViewer);
	}

	/**
	 * Fills the context menu (right-click) of the hierarchy viewer
	 */
	private void fillHierarchyContextMenu(IMenuManager mgr) {
		addGroupsToMenu(mgr);
		addGlobalActionsToMenu(mgr);
		addHierarchyItemsToMenu(mgr);
		addLayoutSubmenuToMenu(mgr);
	}

	
	private void createMemberContextMenu() {
		MenuManager mgr = new MenuManager();
		mgr.setRemoveAllWhenShown(true);
		mgr.addMenuListener(new IMenuListener(){
			@Override
         public void menuAboutToShow(IMenuManager manager) {
				fillMemberContextMenu(manager);
			}
		});
		Menu menu = mgr.createContextMenu(memberViewer.getControl());
		memberViewer.getControl().setMenu(menu);

		// Register context menu for extension (other plugins might add items to this context menu!)
		//getSite().registerContextMenu(mgr, memberViewer);
	}
	
	/**
	 * Fills the context menu (right-click) of the member viewer
	 */
	private void fillMemberContextMenu(IMenuManager mgr) {
		addGroupsToMenu(mgr);
		addGlobalActionsToMenu(mgr);
		addMemberItemsToMenu(mgr);
		addLayoutSubmenuToMenu(mgr);
		Filters.addGlobalFilters(memberViewer, mgr);
		Filters.addModifierFiltersSubmenu(memberViewer, mgr);
	}

	/**
	 * 
	 */
	private void createToolbar() {
		IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
		addGroupsToMenu(mgr);
		addGlobalActionsToMenu(mgr);
		addHierarchyItemsToMenu(mgr);
		addMemberItemsToMenu(mgr);
		addLayoutItemsToMenu(mgr);
		Filters.addGlobalFilters(memberViewer, mgr);
	}

	/**
	 * 
	 */
	private void createMenu() {
		IMenuManager menuMgr = getViewSite().getActionBars().getMenuManager();
		fillMenu(menuMgr);
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener(){
			@Override
         public void menuAboutToShow(IMenuManager mgr) {
				fillMenu(mgr);
			};
		});
	}
	
	private static final String menuGroup_General = "General";
	private static final String menuGroup_Hierarchy = "Hierarchy";
	private static final String menuGroup_Members = "Members";
	private static final String menuGroup_Layout = "Layout";
	
	/**
	 * Adds the appropriate actions to the menu
	 * 
	 * @param mgr
	 */
	private void fillMenu(IMenuManager mgr){
		addGroupsToMenu(mgr);
		addGlobalActionsToMenu(mgr);
		addHierarchyItemsToMenu(mgr);
		addMemberItemsToMenu(mgr);
		addLayoutSubmenuToMenu(mgr);
		Filters.addGlobalFilters(memberViewer, mgr);
		Filters.addModifierFiltersSubmenu(memberViewer, mgr);
	}
	
	/**
	 * Used to add the action to the menu and the toolbar. 
	 * These actions should be common for both ContributorManagers
	 * 
	 * @param mgr the IContributionManager of the menu or toolbar
	 */
	private void addGroupsToMenu(IContributionManager mgr){
		mgr.add(new Separator(menuGroup_General));
		mgr.add(new Separator(menuGroup_Hierarchy));
		mgr.add(new Separator(menuGroup_Members));
		mgr.add(new Separator(menuGroup_Layout));
		mgr.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void addGlobalActionsToMenu(IContributionManager mgr) {
		mgr.appendToGroup(menuGroup_General, subHierarchyAction);
		mgr.appendToGroup(menuGroup_General, superHierarchyAction);
	}

	private void addLayoutItemsToMenu(IContributionManager mgr) {
		mgr.appendToGroup(menuGroup_Layout, new HierarchyActions.ViewVerticalAction(sashForm));
		mgr.appendToGroup(menuGroup_Layout, new HierarchyActions.ViewHorizontalAction(sashForm));
		mgr.appendToGroup(menuGroup_Layout, new HierarchyActions.ViewOnlyHierarchyAction(sashForm));
	}
	
	private void addLayoutSubmenuToMenu(IContributionManager mgr) {
		IMenuManager submenuMgr = new MenuManager("Layout");
		submenuMgr.add(new HierarchyActions.ViewVerticalAction(sashForm));
		submenuMgr.add(new HierarchyActions.ViewHorizontalAction(sashForm));
		submenuMgr.add(new HierarchyActions.ViewOnlyHierarchyAction(sashForm));
		mgr.appendToGroup(menuGroup_Layout, submenuMgr);
	}
	
	private void addMemberItemsToMenu(IContributionManager mgr) {
		mgr.appendToGroup(menuGroup_Members, new HierarchyActions.ShowInheritedMembersAction(memberViewer));
		mgr.appendToGroup(menuGroup_Members, new Separator(Filters.FILTER_GROUP_NAME));
	}

	private void addHierarchyItemsToMenu(IContributionManager mgr) {
		mgr.appendToGroup(menuGroup_Hierarchy, new TreeViewerActions.ExpandAllAction(hierarchyViewer));
		mgr.appendToGroup(menuGroup_Hierarchy, new TreeViewerActions.CollapseAllAction(hierarchyViewer));
		mgr.appendToGroup(menuGroup_Hierarchy, new HierarchyActions.RefreshAction(this));
		mgr.appendToGroup(menuGroup_Hierarchy, new TreeViewerActions.ClearViewerAction(hierarchyViewer, memberViewer));
	}
	
	/**
	 * @return an new hierarchy viewer, null if no IViewDescriptor for the hierarchy view is found
	 * @deprecated doesn't return the existing hierarchyView, but a new instance
	 */
	@Deprecated
   public static HierarchyView getHierarchyView(){
		try {
			IViewDescriptor viewDesc = Workbench.getInstance().getViewRegistry().find("chameleon.eclipse.ChameleonEditorPlugin.hierarchyview");
			if(viewDesc!=null){
				IViewPart view = viewDesc.createView();
				if(view instanceof HierarchyView){
					return (HierarchyView)view;
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
		} catch(NullPointerException e){
			// Workbench.getInstance() might be null
			e.printStackTrace();
		}
		return null;
	}
	
	
}
