package org.aikodi.chameleon.util.action;

import be.kuleuven.cs.distrinet.rejuse.action.Action;
import be.kuleuven.cs.distrinet.rejuse.tree.TreeStructure;

/**
 * A class of objects for traversing a tree structure. In addition
 * to performing an action on a node, a tree action can also perform
 * action before and after visiting a node.
 * 
 * @author Marko van Dooren
 *
 * @param <T> The type of objects in the data structure being traversed.
 * @param <E> The type of exceptions that can be thrown by this walker.
 */
public abstract class TreeAction<T, E extends Exception> extends Action<T,E> {

	/**
	 * Create a new tree action that performs actions on objects of the given type.
	 * 
	 * @param type The class object of the object type that is handled by the walker.
	 */
 /*@
   @ public behavior
   @
   @ pre type != null;
   @
   @ post type() == type;
   @*/
	public TreeAction(Class<T> type) {
		super(type);
	}

	/**
	 * This method is called when a tree action arrives at a node. Code
	 * that must be executed before {@link #perform(Object)} must be
	 * in this method.
	 * 
	 * This method performs the type check and then invokes {@link #doEnter(Object)}.
   *
	 * @param node The data structure node that has just been entered.
	 */
	public void enter(Object node) {
		if(type().isInstance(node)) {
			doEnter((T)node);
		}
	}
	
	/**
	 * Invoked when entering a particular node in the data structure.
	 * The default implementation does nothing.
	 * 
	 * @param node The node that has been entered.
	 */
	public void doEnter(T node) {
	}
	
	
	/**
	 * This method is called when a tree action exits a node. Code
	 * that must be executed after {@link #perform(Object)} must be
	 * in this method.
	 * 
	 * This method performs the type check and then invokes {@link #doExit(Object)}.
   *
	 * @param node The data structure node that has just been exited.
	 */
	public void exit(Object object) {
		if(type().isInstance(object)) {
			doExit((T)object);
		}
	}
	
	/**
	 * Invoked when exiting a particular node in the data structure.
	 * The default implementation does nothing.
	 * 
	 * @param node The node that has been exited.
	 */
	public void doExit(T node) {
	}
	
	public void traverse(T element, TreeStructure<? extends T> tree) throws E {
		perform(element);
	}
}
