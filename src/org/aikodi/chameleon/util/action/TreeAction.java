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
public abstract class TreeAction<T, E extends Exception> {

  /**
   * Return a class object that represents the type of
   * objects on which this action can operate.
   */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
  public Class<T> type() {
    return _type;
  }
  
  private Class<T> _type;
  

  /**
   * Perform the action.
   * 
   * @param tree The tree on which the action must be performed.
   * @throws E
   */
  protected abstract <X extends T> void doPerform(TreeStructure<X> tree) throws E;
  
  /**
   * Perform the action on the given object. First,
   * the type of the object is checked. If it
   * is of type T, the action is applied. Otherwise,
   * nothing is done.
   * 
   * @param tree The object to which the action should
   *               be applied.
   * @throws E
   */
  public <X extends T> void perform(TreeStructure<X> tree) throws E {
    if(type().isInstance(tree.node())) {
      doPerform(tree);
    }
  }
  

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
	  if(type == null) {
	    throw new IllegalArgumentException("The type of a tree action cannot be null.");
	  }
		_type = type;
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
	
	public <X extends T> void traverse(TreeStructure<X> tree) throws E {
		perform(tree);
	}
}
