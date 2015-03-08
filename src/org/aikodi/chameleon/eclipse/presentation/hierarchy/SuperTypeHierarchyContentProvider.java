/**
 * Created on 24-apr-07
 * @author Tim Vermeiren
 */
package org.aikodi.chameleon.eclipse.presentation.hierarchy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.eclipse.project.ChameleonProjectNature;
import org.aikodi.chameleon.exception.ModelException;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.inheritance.InheritanceRelation;

import be.kuleuven.cs.distrinet.rejuse.java.collections.Visitor;

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
				final Collection<Declaration> result = new ArrayList<Declaration>();
				List<InheritanceRelation> inheritanceRelations = type.inheritanceRelations();
				// van elke typereference het type opvragen en aan het resultaat toevoegen:
				new Visitor<InheritanceRelation>(){
					@Override
               public void visit(InheritanceRelation element) {
						try {
							result.add(element.superElement());
						} catch (ModelException e) {
							e.printStackTrace();
						}
					}
				}.applyTo(inheritanceRelations);
				
				Declaration[] typeArray = result.toArray(new Declaration[]{});
				return HierarchyTypeNode.encapsulateInHierarchyTreeNodes(typeArray, projectNature, parentTypeNode);

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
