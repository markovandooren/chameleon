/**
 * Created on 24-apr-07
 * @author Tim Vermeiren
 */
package org.aikodi.chameleon.eclipse.presentation.hierarchy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.eclipse.project.ChameleonProjectNature;
import org.aikodi.chameleon.exception.ModelException;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.inheritance.InheritanceRelation;
import org.aikodi.rejuse.java.collections.Visitor;

/**
 * This class will calculate the children of an element in the super type hierarchy
 * 
 * @author Tim Vermeiren
 */
public class SuperTypeHierarchyContentProvider extends HierarchyContentProvider {

	public SuperTypeHierarchyContentProvider() {
	}

	/**
	 * Returns all supertypes of the element if it's a type
	 */
	@Override
   public Object[] getChildren(Object element) {
		if(element instanceof HierarchyTypeNode){
			try {
				HierarchyTypeNode parentTypeNode = (HierarchyTypeNode)element;
				ChameleonProjectNature projectNature = parentTypeNode.getProjectNature();
				Type type = parentTypeNode.getType();
				List<InheritanceRelation> inheritanceRelations = type.inheritanceRelations();
				List<Type> types = new ArrayList<>();
				for(InheritanceRelation relation: inheritanceRelations) {
					// We want to provide as much information as possible.
					// Therefore we do a separate lookup for each inheritance relation
					// and continue on exceptions.
					try {
						types.add(relation.superType());
					} catch(LookupException exc) {
						exc.printStackTrace();
					}
				}
				return HierarchyTypeNode.encapsulateInHierarchyTreeNodes(types, projectNature, parentTypeNode);

			} catch (ModelException e) {
				e.printStackTrace();
			}
		} else if(element instanceof RootType){
			return ((RootType)element).getChildren();
		}
		return new Object[]{};
	}
	
	/**
	 * Returns the parent of the given element
	 */
	@Override
   public Object getParent(Object element) {
		if(element instanceof HierarchyTypeNode){
			HierarchyTypeNode node = (HierarchyTypeNode)element;
			return node.getParent();
		}
		return null;
	}



}
