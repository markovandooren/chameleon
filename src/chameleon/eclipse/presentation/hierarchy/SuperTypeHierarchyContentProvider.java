/**
 * Created on 24-apr-07
 * @author Tim Vermeiren
 */
package chameleon.eclipse.presentation.hierarchy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.rejuse.java.collections.Visitor;

import chameleon.core.declaration.Declaration;
import chameleon.eclipse.project.ChameleonProjectNature;
import chameleon.exception.ModelException;
import chameleon.oo.type.Type;
import chameleon.oo.type.inheritance.InheritanceRelation;

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
