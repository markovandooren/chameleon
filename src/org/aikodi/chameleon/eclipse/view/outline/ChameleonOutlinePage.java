/**
 * 
 */
package org.aikodi.chameleon.eclipse.view.outline;

import java.util.List;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.language.Language;
import org.aikodi.chameleon.eclipse.ChameleonEditorPlugin;
import org.aikodi.chameleon.eclipse.editors.ChameleonEditor;
import org.aikodi.chameleon.eclipse.presentation.Filters;
import org.aikodi.chameleon.eclipse.presentation.treeview.ChameleonLabelProvider;
import org.aikodi.chameleon.eclipse.presentation.treeview.ChameleonViewComparator;
import org.aikodi.chameleon.eclipse.presentation.treeview.TreeViewerActions;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

/**
 * @author Manuel Van Wesemael 
 * @author Joeri Hendrickx 
 * 
 * A class to create the outline view for the chameleonFramework. 
 * The outline is generated language independently through use of XML files: language elements, icons, etc are loaded here.
 * A standard treeviewer is used to view the tree structure that was created.
 * The contents & labels come from the ChameleonTreeContentsProvider & ChameleonLabelProvider 
 */
public class ChameleonOutlinePage extends ContentOutlinePage {
	
	//the current language used in the editor
	private Language _currentLanguage;

	//The treeviewer that is used to store and view our tree
	private TreeViewer _treeViewer;

	//the editor where this ChameleonContentOutlinePage is used
	private ChameleonEditor _editor;

	//The chameleonTree used by this content outline
	private ChameleonOutlineTree _chameleonTree;

	// Wheter to show the root-element of the tree, or not
	private boolean _showRoot = false;

	/**
	 * The creation of a ChameleonContentOutlinePage with given language & editor
	 * 
	 * @param language
	 * 		The language that is currently used. 
	 * 		If the language is not available for the outline, no outline is created 
	 * @param allowedElements
	 * 		The editor tho which this ChameleonContentOutlinePage is linked.
	 * 		If the editor is not effective, no outline is created
	 * @param defaultAllowedElements
	 * 		the default allowed elements
	 */
	public ChameleonOutlinePage(Language language, ChameleonEditor editor, List<String> allowedElements, List<String> defaultAllowedElements) {
		super();
		try {
			if(editor == null) {
				throw new NullPointerException("editor is not available, ouline is not created");
			}
			this._editor = editor;
			changeLanguage(language);
		} catch (NullPointerException npe){
			System.err.println(npe.getMessage());
		}
		//PresentationModel.initAllowedOutlineElementsByDefaults(language.name(), allowedElements, defaultAllowedElements);
	}

	/**
	 * Changes the currently used language to the given one, if the new one is supported
	 * Then the language elements for the new language are loaded
	 * @param language
	 * 	The language to be switched at
	 * 	 */
	public void changeLanguage(Language language) {
		if(_currentLanguage != null && ! _currentLanguage.equals(language)){
			updateMenu();
		}
		_currentLanguage = language;
	}

	/**
	 * 
	 * @return the current language used
	 */
	public Language language() {
		return _currentLanguage;
	}

	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		//creates the treeViewer
		_treeViewer = getTreeViewer();

		// Add drag/drop support for the tree viewer
		//------------------------------------------
		//		int ops = DND.DROP_MOVE;
		//
		//		Transfer[] transfers = new Transfer[] { ScriptUIEditorIntegerTransfer.getInstance()};
		//
		//		treeViewer.addDragSupport(ops, transfers, new ScriptUIEditorTreeViewerDragAdapter(treeViewer));
		//		treeViewer.addDropSupport(ops, transfers, new ScriptUIEditorTreeViewerDropAdapter(treeViewer, this));

		// Add tree viewer listeners
		//--------------------------
		_treeViewer.addSelectionChangedListener(this);

		// Create content providers for the tree viewer
		//---------------------------------------------
		_treeViewer.setContentProvider(new ChameleonOutlineTreeContentProvider());
		ChameleonLabelProvider chameleonLabelProvider = new ChameleonLabelProvider(_currentLanguage, true, false, false);
		_treeViewer.setLabelProvider(new DecoratingLabelProvider(chameleonLabelProvider,null)); 

		// Set initial expansion level
		//----------------------------
		_treeViewer.setAutoExpandLevel(AbstractTreeViewer.ALL_LEVELS);

		// Add menu
		//----------------------------
/*		IMenuManager menuMgr = getSite().getActionBars().getMenuManager();
		//menuMgr.setRemoveAllWhenShown(true);
		createMenuActions(menuMgr);
		menuMgr.addMenuListener(new IMenuListener(){
			public void menuAboutToShow(IMenuManager mgr) {
				mgr.removeAll();
				createMenuActions(mgr);
			};
		});*/
		MenuManager menuMgr = new MenuManager();
		menuMgr.setRemoveAllWhenShown(true);
		createMenuActions(menuMgr);
		menuMgr.addMenuListener(new IMenuListener(){
			@Override
         public void menuAboutToShow(IMenuManager mgr) {
				mgr.removeAll();
				createMenuActions(mgr);
			};
		});
		
		Menu menu = menuMgr.createContextMenu(_treeViewer.getControl());
		_treeViewer.getControl().setMenu(menu);
		
		getSite().registerContextMenu("chameleonOutlineMenu", menuMgr, _treeViewer);

		// Add Toolbar
		//----------------------------
		IToolBarManager ToolbarMgr = getSite().getActionBars().getToolBarManager();
		ToolbarMgr.add(new Separator("Sorting"));
		ToolbarMgr.add(new SortMembersAction(_treeViewer));
		// mgr.add(new RefreshAction(this)); // show refresh-button in toolbar
		ToolbarMgr.add(new Separator(Filters.FILTER_GROUP_NAME));
		ToolbarMgr.add(new Separator());

		Filters.addGlobalFilters(_treeViewer, ToolbarMgr);

		// Build tree
		//----------------------------
		buildTree();
	}

	/**
	 * The modifier filters-submenu must be updated if the language is changed
	 */
	protected void updateMenu(){
		IMenuManager menuMgr = getSite().getActionBars().getMenuManager();
		menuMgr.removeAll();
		createMenuActions(menuMgr);
	}
	protected void createMenuActions(IMenuManager mgr) {
		mgr.add(new Separator("Sorting"));
		mgr.add(new SortMembersAction(_treeViewer));
		mgr.add(new Separator("TreeViewer actions"));
		mgr.add(new TreeViewerActions.ExpandAllAction(_treeViewer));
		mgr.add(new TreeViewerActions.CollapseAllAction(_treeViewer));
		mgr.add(new TreeViewerActions.RefreshAction(_treeViewer));
		mgr.add(new Separator("Outline actions"));
		mgr.add(new ShowRootAction(this));
		mgr.add(new hideTypesAction());
		mgr.add(new Separator(Filters.FILTER_GROUP_NAME));
		// here the filters will be added
		mgr.add(new Separator());

		Filters.addGlobalFilters(_treeViewer, mgr);
		Filters.addModifierFiltersSubmenu(_treeViewer, mgr, _currentLanguage);
	}

	public class ShowRootAction extends Action{
		private ChameleonOutlinePage outline;
		public ShowRootAction(ChameleonOutlinePage outline){
			super("Show the root element", AS_CHECK_BOX);
			this.outline = outline;
			if(outline._showRoot){
				setChecked(true);
			}
		}
		@Override
		public void run() {
			outline._showRoot = ! outline._showRoot;
			setChecked(_showRoot);
			buildTree();
		}
	}

	public ChameleonLabelProvider chameleonLabelProvider() {
		return (ChameleonLabelProvider) ((DecoratingLabelProvider)_treeViewer.getLabelProvider()).getLabelProvider();
	}
	
	public class hideTypesAction extends Action {
		public hideTypesAction(){
			super("Hide defining types", AS_CHECK_BOX);
			setChecked(!chameleonLabelProvider().isShowingDefiningTypes());
		}
		@Override
		public void run() {
			chameleonLabelProvider().invertShowDefiningType();
			_treeViewer.refresh();
		}
	}

	public class SortMembersAction extends Action {
		private TreeViewer treeViewer;
		public SortMembersAction(TreeViewer treeViewer){
			super("Sort elements by category and name", AS_CHECK_BOX);
			setImageDescriptor(ChameleonEditorPlugin.getImageDescriptor("alphabetical_sort.gif"));
			this.treeViewer = treeViewer;
			if(outlineComparator().equals(treeViewer.getComparator())){
				setChecked(true);
			}
		}
		@Override
		public void run() {
			ChameleonViewComparator outlineComparator = outlineComparator();
			if(! outlineComparator.equals(treeViewer.getComparator())){
				// if not yet sorted
				treeViewer.setComparator(outlineComparator);
			} else {
				treeViewer.setComparator(null);
			}
		}
	}
	
	public ChameleonViewComparator outlineComparator() {
		return new ChameleonViewComparator();
	}

	/**
	 * This responds to a SelectionChangedEvent.
	 * When another element in the ChameleonTree is selected, 
	 * a highlight of the element is made.
	 */
	@Override
   public void selectionChanged(SelectionChangedEvent event) {
		super.selectionChanged(event);

		ISelection selection= event.getSelection();
		if (selection.isEmpty())
			_editor.resetHighlightRange();
		else {
			ChameleonOutlineTree segment= (ChameleonOutlineTree) ((IStructuredSelection) selection).getFirstElement();
			Element element = segment.getElement();
			ChameleonEditor.showInEditor(element, false, false, getEditor());
//			_editor.highLightElement(element);
		}
	}

	private void buildTree() {
		try{
			ChameleonOutlineTree newTree = new ChameleonOutlineTree(getTreeRootElement());
			newTree.composeTree();
			if((! newTree.isTainted()) || (_chameleonTree == null || _chameleonTree.isTainted() && _chameleonTree.size() < newTree.size())) {
				_chameleonTree = newTree;
			}
		}
		catch(Exception e){
		}
		try {
			_treeViewer.setInput(_chameleonTree);
			_treeViewer.refresh();
		} catch (RuntimeException e) {
			// During shutdown.
		} 
		catch(Exception e){
			_chameleonTree = new ChameleonOutlineTree();
		}
	}

	/*
	 * returns the root element for the tree
	 */
	private Element getTreeRootElement() {
		Element result = getEditor().getDocument().document();
		// if we don't want to show the root, and the root has one valid child, get this as root element
		if(!_showRoot && result!=null && result.lexical().children()!=null && result.lexical().children().size()==1) {
			result = result.lexical().children().iterator().next();
		}
		return result;
	}

	/**
	 * 
	 * @return the editor where this is used
	 */
	private ChameleonEditor getEditor() {
		return _editor;
	}

	/**
	 * updates the outline by adding the elements added to the model, and by removing
	 * the elements removed at the model
	 * @param added
	 * 	the elements added to the model
	 * @param removed
	 * 	the elements removed at the model
	 */
	public void updateOutline() {
		buildTree();
	}

	@Override
	public void dispose() {
		super.dispose();
	}

}
