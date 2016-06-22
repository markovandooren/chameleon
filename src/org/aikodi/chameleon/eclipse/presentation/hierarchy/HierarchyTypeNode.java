/**
 * Created on 23-mei-07
 * @author Tim Vermeiren
 */
package org.aikodi.chameleon.eclipse.presentation.hierarchy;

import java.util.List;

import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.eclipse.project.ChameleonProjectNature;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeReference;

public class HierarchyTypeNode implements HierarchyTreeNode {
	
	private String fullyQualifiedName;
	
	private ChameleonProjectNature projectNature;
	
	private HierarchyTreeNode parent;
	
	public HierarchyTypeNode(Type type, ChameleonProjectNature projectNature, HierarchyTreeNode parent){
		fullyQualifiedName = type.getFullyQualifiedName();
		this.projectNature = projectNature;
		this.parent = parent;
	}
	
	public Type getType() throws LookupException {
		Type result = null; 
		try {
			TypeReference tref= projectNature.getModel().language(ObjectOrientedLanguage.class).createTypeReference(fullyQualifiedName);
			tref.setUniParent(projectNature.getModel());
			result = tref.getElement();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
   public HierarchyTreeNode getParent(){
		return parent;
	}
	
	public ChameleonProjectNature getProjectNature(){
		return projectNature;
	}
	
	/**
	 * STATIC MEMBERS
	 */
	
	public static HierarchyTypeNode[] encapsulateInHierarchyTreeNodes(List<Type> types, ChameleonProjectNature projectNature, HierarchyTreeNode parent){
		HierarchyTypeNode[] nodes = new HierarchyTypeNode[types.size()];
		for(int i=0; i<types.size(); i++){
			HierarchyTypeNode node = new HierarchyTypeNode(types.get(i), projectNature, parent);
			nodes[i] = node;
		}
		return nodes;
	}
	
}
