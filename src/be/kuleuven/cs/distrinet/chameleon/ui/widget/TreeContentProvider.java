package be.kuleuven.cs.distrinet.chameleon.ui.widget;

import java.util.Collections;
import java.util.List;

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
public abstract class TreeContentProvider<T> {

	public TreeContentProvider(Class<T> type) {
		Contracts.notNull(type, "The type of a tree content provider cannot be null.");
		_type = type;
	}
	
	private Class<T> _type;
	
	/**
	 * Return the class object of the type of objects that can be handled by this content provider.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public Class<T> type() {
		return _type;
	}
	
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
	public abstract List<T> children(T element);
	
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
	public boolean hasChildren(T element) {
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
	public abstract T parent(T element);
	
	/**
	 * Return the parent of the given element.
	 * 
	 * @see #parent(Object)
	 */
	public Object universalParent(Object element) {
		return type().isInstance(element) ?
			parent((T)element) :
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
	public List universalChildren(Object element) {
		return type().isInstance(element) ?
				children((T)element) :
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
		return type().isInstance(element) ?
				hasChildren((T)element) :
			  false;
	}
}
