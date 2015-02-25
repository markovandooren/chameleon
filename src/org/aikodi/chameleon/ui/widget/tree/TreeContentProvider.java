package org.aikodi.chameleon.ui.widget.tree;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.ImmutableList;

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
public abstract class TreeContentProvider<G> {

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
	public abstract List<TreeNode<?,G>> children(TreeNode<?,G> element);
	
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
	public boolean hasChildren(TreeNode<?,G> element) {
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
	public abstract TreeNode<?,G> parent(TreeNode<?,G> element);
	
	public List<TreeNode<?,G>> ancestors(TreeNode<?,G> element) {
		ImmutableList.Builder<TreeNode<?,G>> builder = ImmutableList.builder();
		TreeNode<?,G> ancestor = parent(element);
		while(ancestor != null) {
			builder.add(ancestor);
			ancestor = parent(ancestor);
		}
		return builder.build();
	}
	
	public boolean isAncestor(TreeNode<?,G> ancestor, TreeNode<?,G> descendant) {
		TreeNode<?,G> a = parent(descendant);
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
			parent((TreeNode<?,G>)element) :
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
	public List<TreeNode<?,G>> universalChildren(Object element) {
		return element instanceof TreeNode ?
				children((TreeNode<?,G>)element) :
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
				hasChildren((TreeNode<?,G>)element) :
			  false;
	}
	
	public abstract TreeNode<?,G> createNode(G input);
	
	public G domainData(TreeNode<?,G> treeData) {
		return treeData.domainObject();
	}
}
