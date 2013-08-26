package be.kuleuven.cs.distrinet.chameleon.ui.widget.tree;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.ImmutableList;

import be.kuleuven.cs.distrinet.rejuse.contract.Contracts;

/**
 * A generic interface for providing tree structures 
 * without creating dependencies to frameworks such as
 * AWT, Swing, or SWT.
 * 
 * The universalX methods have the same behavior as the corresponding
 * non-universalX methods when the argument is of type {@link T} . Otherwise
 * these methods will return whatever is appropriate: false, null, an empty collection,
 * ... 
 * 
 * @author Marko van Dooren
 *
 * @param <T> The type of the objects in the tree.
 */
public abstract class TreeContentProvider<D> {

//	public TreeContentProvider(Class<T> type) {
//		Contracts.notNull(type, "The type of a tree content provider cannot be null.");
//		_type = type;
//	}
//	
//	private Class<T> _type;
	
//	/**
//	 * Return the class object of the type of objects that can be handled by this content provider.
//	 */
// /*@
//   @ public behavior
//   @
//   @ post \result != null;
//   @*/
//	public Class<T> type() {
//		return _type;
//	}
	
	/**
	 * Return the children of the given element.
	 * 
	 * @param element The element of which the children are requested.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @ post element == null ==> \result.isEmpty();
   @*/
	public abstract List<TreeNode<D>> children(TreeNode<D> element);
	
	/**
	 * Check whether the given element has children.
	 * 
	 * @param element The element of which must be determined if it has children.
	 */
 /*@
   @ public behavior
   @
   @ post element == null ==> \result == false;
   @*/
	public boolean hasChildren(TreeNode<D> element) {
		return element == null ? false : ! children(element).isEmpty();
	}
	
	/**
	 * Return the parent of the given element.
	 * 
	 * @param element The element of which the parent is returned.
	 */
 /*@
   @ public behavior
   @
   @ post element == null ==> \result == null;
   @*/
	public abstract TreeNode<D> parent(TreeNode<D> element);
	
	public List<TreeNode<D>> ancestors(TreeNode<D> element) {
		ImmutableList.Builder<TreeNode<D>> builder = ImmutableList.builder();
		TreeNode<D> ancestor = parent(element);
		while(ancestor != null) {
			builder.add(ancestor);
			ancestor = parent(ancestor);
		}
		return builder.build();
	}
	
	public boolean isAncestor(TreeNode<D> ancestor, TreeNode<D> descendant) {
		TreeNode<D> a = parent(descendant);
		while(a != null) {
			if(a == ancestor) {
				return true;
			}
			a = parent(a);
		}
		return false;
	}
	
	/**
	 * Return the parent of the given element.
	 * 
	 * @see #parent(Object)
	 */
	public Object universalParent(Object element) {
		return element instanceof TreeNode ?
			parent((TreeNode<D>)element) :
		  null;
	}
	
	/**
	 * Return the children of the given element.
	 * 
	 * @see #children(Object)
	 */
 /*@
   @ public behavior
   @
   @ type().isInstance(element) ==> \result == children((T)element);
   @ ! type().isInstance(element) ==> \result == Collections.EMPTY_LIST;
   @*/
	public List<TreeNode<D>> universalChildren(Object element) {
		return element instanceof TreeNode ?
				children((TreeNode<D>)element) :
			  Collections.EMPTY_LIST;
	}
	
	/**
	 * Return the children of the given element.
	 * 
	 * @see #children(Object)
	 */
 /*@
   @ public behavior
   @
   @ type().isInstance(element) ==> \result == hasChildren((T)element);
   @ ! type().isInstance(element) ==> \result == Collections.EMPTY_LIST;
   @*/
	public boolean universalHasChildren(Object element) {
		return element instanceof TreeNode ?
				hasChildren((TreeNode<D>)element) :
			  false;
	}
	
	public abstract TreeNode<? extends D> createNode(D input);
	
	public D domainData(TreeNode<D> treeData) {
		return treeData.domainObject();
	}
}
