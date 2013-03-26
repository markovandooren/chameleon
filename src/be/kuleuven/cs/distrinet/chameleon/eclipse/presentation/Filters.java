/**
 * Created on 30-apr-07
 * @author Tim Vermeiren
 */
package be.kuleuven.cs.distrinet.chameleon.eclipse.presentation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.language.Language;
import be.kuleuven.cs.distrinet.chameleon.core.modifier.ElementWithModifiers;
import be.kuleuven.cs.distrinet.chameleon.core.modifier.Modifier;
import be.kuleuven.cs.distrinet.chameleon.core.namespacedeclaration.NamespaceDeclaration;
import be.kuleuven.cs.distrinet.chameleon.eclipse.ChameleonEditorPlugin;
import be.kuleuven.cs.distrinet.chameleon.eclipse.connector.EclipseEditorExtension;
import be.kuleuven.cs.distrinet.chameleon.eclipse.presentation.treeview.ChameleonLabelProvider;
import be.kuleuven.cs.distrinet.chameleon.eclipse.presentation.treeview.TreeViewerActions;
import be.kuleuven.cs.distrinet.chameleon.eclipse.project.ChameleonProjectNature;
import be.kuleuven.cs.distrinet.chameleon.exception.ModelException;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.oo.variable.MemberVariable;
import be.kuleuven.cs.distrinet.chameleon.util.Util;

/**
 * Contains the filters for the structuredViewers (outline and hierarchy)
 * 
 * @author Tim Vermeiren
 */
public class Filters {

	/**
	 * FILTER CLASSES
	 */

	public static class FieldsFilterAction extends Action {
		private StructuredViewer memberViewer;
		// constructor:
		public FieldsFilterAction(StructuredViewer memberViewer) {
			super("Hide fields", AS_CHECK_BOX);
			setImageDescriptor(ChameleonEditorPlugin.getImageDescriptor("show_nonfields.gif"));
			this.memberViewer = memberViewer;
			if(Util.arrayContains(memberViewer.getFilters(), fieldsFilter)){
				setChecked(true);
			}
		}
		@Override
		public void run() {
			if(Util.arrayContains(memberViewer.getFilters(), fieldsFilter)){
				memberViewer.removeFilter(fieldsFilter);
				setChecked(false);
			} else {
				memberViewer.addFilter(fieldsFilter);
				setChecked(true);
			}
		}
	}

	private static final ViewerFilter fieldsFilter = new ViewerFilter(){
		public boolean select(Viewer viewer, Object parentObject, Object object) {
			Element element = ChameleonLabelProvider.getElement(object);
			return ! (element instanceof MemberVariable);
		}
	};

	public static class AllAccessibleFilterAction extends Action {
		private StructuredViewer memberViewer;
		// constructor:
		public AllAccessibleFilterAction(StructuredViewer memberViewer) {
			super("Show only all-accessible members", AS_CHECK_BOX);
			setImageDescriptor(ChameleonEditorPlugin.getImageDescriptor("show_public.gif"));
			this.memberViewer = memberViewer;
			if(Util.arrayContains(memberViewer.getFilters(), allAccessibleFilter)){
				setChecked(true);
			}
		}
		@Override
		public void run() {
			if(Util.arrayContains(memberViewer.getFilters(), allAccessibleFilter)){
				memberViewer.removeFilter(allAccessibleFilter);
				setChecked(false);
			} else {
				memberViewer.addFilter(allAccessibleFilter);
				setChecked(true);
			}
		}
	}

	private static final ViewerFilter allAccessibleFilter = new ViewerFilter(){
		/**
		 * Select only the NamespaceElements that are at least accessible in their own
		 * namespace
		 */
		@Override
		public boolean select(Viewer viewer, Object parentElement, Object object) {
			Element element = ChameleonLabelProvider.getElement(object);
			
			if(element instanceof Declaration){
				try {
					Declaration decl = (Declaration)element;
					return decl.scope().contains(decl.nearestAncestor(NamespaceDeclaration.class).namespace());
				} catch (ModelException e) {
					e.printStackTrace();
					return false;
				}
			}
			return false;
		}

	};
	public static class ModifierFilterAction extends Action {
		private StructuredViewer memberViewer;
		private ModifierFilter filter;
		// constructor:
		public ModifierFilterAction(StructuredViewer memberViewer, Modifier modifier) {
			super("Hide "+modifier.getClass().getSimpleName()+" members", AS_CHECK_BOX);
			this.memberViewer = memberViewer;
			this.filter = getModifierFilter(modifier);
			if(Util.arrayContains(memberViewer.getFilters(), filter)){
				setChecked(true);
			}
		}
		@Override
		public void run() {
			if(Util.arrayContains(memberViewer.getFilters(), filter)){
				memberViewer.removeFilter(filter);
				setChecked(false);
			} else {
				memberViewer.addFilter(filter);
				setChecked(true);
			}
		}
	}

	private static Map<Modifier, ModifierFilter> modifierFilters = new HashMap<Modifier, ModifierFilter>();

	private static ModifierFilter getModifierFilter(Modifier modifier){
		// use cashing, so for every modifier, only one modifierFilter is made
		ModifierFilter result = modifierFilters.get(modifier);
		if(result == null){
			result = new ModifierFilter(modifier);
			modifierFilters.put(modifier, result);
		}
		return result;
	}

	private static class ModifierFilter extends ViewerFilter{
		public Modifier modifier;
		// Constructor
		public ModifierFilter(Modifier modifier) {
			this.modifier = modifier;
		}
		@Override
		public boolean select(Viewer viewer, Object parentElement, Object object) {
			Element element = ChameleonLabelProvider.getElement(object);
			if(element instanceof ElementWithModifiers && !(element instanceof Type) ){
//				return ! ((ElementWithModifiers)element).modifiers().contains(modifier);
				for(Modifier mod: ((ElementWithModifiers)element).modifiers()) {
					if(mod.getClass().equals(modifier.getClass())) {
						return false;
					}
				}
			}
			return true;
		}
		/**
		 * ModifierFilter's for the same modifier are equal.
		 * So we can check if a modifierfilter is already active in a StructuredViewer
		 * for a given modifier 
		 */
		@Override
		public boolean equals(Object other){
			if(other.getClass().equals(this.getClass()) ){
				return modifier.equals(((ModifierFilter)other).modifier);
			}
			return false;
		}

		@Override
		public int hashCode() {
			return modifier.hashCode();
		}
	}

	public static class RemoveAllFiltersAction extends Action {
		private StructuredViewer memberViewer;
		// constructor:
		public RemoveAllFiltersAction(StructuredViewer memberViewer) {
			super("Remove all filters", AS_PUSH_BUTTON);
			setImageDescriptor(ChameleonEditorPlugin.getImageDescriptor("removeall.gif"));
			this.memberViewer = memberViewer;
		}
		@Override
		public void run() {
			memberViewer.resetFilters();
			new TreeViewerActions.RefreshAction(memberViewer).run();
		}
	}
	
	

	/**
	 * METHODS TO ADD FILTER ACTIONS
	 */
	
	public static final String FILTER_GROUP_NAME = "Filters";
	
	public static void addGlobalFilters(StructuredViewer viewer, IContributionManager mgr){
		mgr.appendToGroup(FILTER_GROUP_NAME, new FieldsFilterAction(viewer));
		mgr.appendToGroup(FILTER_GROUP_NAME, new AllAccessibleFilterAction(viewer));
		mgr.appendToGroup(FILTER_GROUP_NAME, new RemoveAllFiltersAction(viewer));

	}

	public static void addModifierFiltersSubmenu(StructuredViewer viewer, IContributionManager mgr){
		Language language = ChameleonProjectNature.getCurrentLanguage();
		addModifierFiltersSubmenu(viewer, mgr, language);
	}

	/**
	 * @pre		the global filter actions must be added first to the ContributionManager
	 * @param 	viewer
	 * @param 	mgr
	 * @param 	language
	 */
	public static void addModifierFiltersSubmenu(StructuredViewer viewer, IContributionManager mgr, Language language){
		if(language != null){
			IMenuManager filtersSubmenu = new MenuManager("Modifier filters (" + language.name()+")");
			EclipseEditorExtension ext = language.plugin(EclipseEditorExtension.class);
			if(ext != null){
				List<Modifier> modifiers = ext.getFilterModifiers();
				for(Modifier mod : modifiers){
					filtersSubmenu.add(new Filters.ModifierFilterAction(viewer, mod));
				}
			}
			// add submenu to the menu in the filters-group
			mgr.appendToGroup(FILTER_GROUP_NAME, filtersSubmenu);
		}
	}

	
}
