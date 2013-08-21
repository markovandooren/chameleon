package be.kuleuven.cs.distrinet.chameleon.ui.widget;

import java.util.List;

/**
 * A generic interface for providing tree structures 
 * without creating dependencies to frameworks such as
 * AWT, Swing, or SWT.
 * 
 * @author Marko van Dooren
 */
public interface TreeContentProvider<T> {

	/**
	 * Return the children of the given element.
	 * 
	 * @param element The element of which the children are requested.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @ post element == null => \result.isEmpty();
   @*/
	public List<T> children(T element);
}
