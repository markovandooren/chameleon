/**
 * Created on 24-apr-07
 * @author Tim Vermeiren
 */
package be.kuleuven.cs.distrinet.chameleon.eclipse.presentation.hierarchy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.eclipse.project.ChameleonProjectNature;
import be.kuleuven.cs.distrinet.chameleon.exception.ModelException;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.oo.type.inheritance.InheritanceRelation;
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
	public Object getParent(Object element) {
		if(element instanceof HierarchyTypeNode){
			HierarchyTypeNode node = (HierarchyTypeNode)element;
			return node.getParent();
		}
		return null;
	}



}
