/**
 * Created on 24-apr-07
 * @author Tim Vermeiren
 */
package org.aikodi.chameleon.eclipse.presentation.hierarchy;

import java.util.List;

import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.namespace.Namespace;
import org.aikodi.chameleon.eclipse.project.ChameleonProjectNature;
import org.aikodi.chameleon.exception.ModelException;
import org.aikodi.chameleon.oo.type.Type;

import be.kuleuven.cs.distrinet.rejuse.predicate.Predicate;
import be.kuleuven.cs.distrinet.rejuse.predicate.SafePredicate;

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
	
	/**
	 * Calculates the children of the given parentElement
	 */
	@Override
   public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof HierarchyTypeNode){
			try {
				// unwrap Type out hierarchyTypeNode:
				HierarchyTypeNode parentTypeNode = ((HierarchyTypeNode)parentElement);
				ChameleonProjectNature projectNature = parentTypeNode.getProjectNature();
				Type type = parentTypeNode.getType();
				Predicate<Type,LookupException> predicate = t -> {
					// We want to provide as much information as possible,
					// so we continue when we encounter an exception.
					try {
						return t.subtypeOf(type);
					}catch(LookupException exc) {
						exc.printStackTrace();
						return false;
					}
				};
				List<Type> types = rootNamespace.descendants(predicate.makeUniversal(Type.class));
				// wrap subtypes in HierarchyTypeNode[]
				return HierarchyTypeNode.encapsulateInHierarchyTreeNodes(types, projectNature, parentTypeNode);
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
				List<Type> directSuperTypes = type.getDirectSuperTypes();
				return directSuperTypes.contains(this.superType);
			} catch (ModelException e) {
				e.printStackTrace();
				return false;
			}
		}
	}

}
