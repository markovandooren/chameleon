/**
 * Created on 23-mei-07
 * @author Tim Vermeiren
 */
package be.kuleuven.cs.distrinet.chameleon.eclipse.presentation.hierarchy;


/**
 * A node of the hierarchy tree. This can be a Type(wrapper) or a RootItem.
 * Every element in the hierarchy tree must implement this interface.
 * 
 * @author Tim Vermeiren
 */
public interface HierarchyTreeNode {
	
	HierarchyTreeNode getParent();
	
}
