/**
 * Created on 24-apr-07
 * @author Tim Vermeiren
 */
package org.aikodi.chameleon.eclipse.presentation.hierarchy;

import org.aikodi.chameleon.core.document.Document;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.namespace.Namespace;
import org.aikodi.chameleon.eclipse.project.ChameleonProjectNature;
import org.aikodi.chameleon.exception.ModelException;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.workspace.InputException;
import org.aikodi.rejuse.predicate.Predicate;
import org.aikodi.rejuse.predicate.SafePredicate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class will calculate the children of an element in the sub type hierarchy
 * 
 * @author Tim Vermeiren
 */
public class SubTypeHierarchyContentProvider extends HierarchyContentProvider {

	private Namespace rootNamespace;

	public SubTypeHierarchyContentProvider(Namespace rootNamespace) {
		this.rootNamespace = rootNamespace;
	}

	private Map<Type, List<Type>> _cache;

	public void flushCache() {
		_cache = null;
	}

	/**
	 * Calculates the children of the given parentElement
	 */
	@Override
	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof HierarchyTypeNode){
			try {
				HierarchyTypeNode parentTypeNode = ((HierarchyTypeNode)parentElement);
				ChameleonProjectNature projectNature = parentTypeNode.getProjectNature();
				Type type = parentTypeNode.getType();
				if(_cache != null) {
					List<Type> types = _cache.get(type);
					if(types != null) {
						return HierarchyTypeNode.encapsulateInHierarchyTreeNodes(types, projectNature, parentTypeNode);
					}
				} else {
					_cache = new HashMap<>();
				}
				// unwrap Type out hierarchyTypeNode:
				List<Type> types = subtypes(type);
				// wrap subtypes in HierarchyTypeNode[]
				HierarchyTypeNode[] encapsulateInHierarchyTreeNodes = HierarchyTypeNode.encapsulateInHierarchyTreeNodes(types, projectNature, parentTypeNode);
				_cache.put(type, types);
				return encapsulateInHierarchyTreeNodes;
			} catch (Exception e){
				e.printStackTrace();
				return new Object[]{};
			}

		} else if(parentElement instanceof RootType){
			return ((RootType)parentElement).getChildren();
		}
		return new Object[]{};
	}

	/**
	 * Find the subtypes of the given type.
	 * 
	 * @param type
	 * @return
	 * @throws LookupException
	 */
	private List<Type> subtypes(Type type) throws LookupException {
		Element origin = type.origin();
		if(origin instanceof Type) {
			final Type originalType = (Type) origin;
			Predicate<Type,LookupException> predicate = t -> {
				// We want to provide as much information as possible,
				// so we continue when we encounter an exception.
				try {
					return ! t.origin().sameAs(originalType) && t.subtypeOf(type);
				}catch(LookupException exc) {
					return false;
				}
			};
			final List<Type> types = new ArrayList<>();
			try {
				List<Document> sourceDocuments = rootNamespace.view().sourceDocuments();
				for(Document d: sourceDocuments) {
					try {
						List<Type> descendants = d.lexical().descendants(Type.class, predicate);
						types.addAll(descendants);
					} catch (LookupException e) {
					}
				}
			} catch (InputException e) {
				e.printStackTrace();
			}
			return types;
		} else {
			return new ArrayList<>();
		}
	}

	/**
	 * Calculates the parent of the given element:
	 */
	@Override
	public Object getParent(Object element) {
		if(element instanceof HierarchyTreeNode){
			return ((HierarchyTreeNode)element).getParent();
		}
		return null;
	}

	private class hasAsSuperTypePredicate extends SafePredicate<Type>{
		private Type superType;
		public hasAsSuperTypePredicate(Type superType) {
			this.superType = superType;
		}

		@Override
		public boolean eval(Type type) {
			try {
				List<Type> directSuperTypes = type.getProperDirectSuperTypes();
				return directSuperTypes.contains(this.superType);
			} catch (ModelException e) {
				e.printStackTrace();
				return false;
			}
		}
	}

}
