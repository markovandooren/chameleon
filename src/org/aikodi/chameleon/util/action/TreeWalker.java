package org.aikodi.chameleon.util.action;

import org.aikodi.rejuse.tree.TreeStructure;
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
public interface TreeWalker<T, E extends Exception> {

  /**
   * Perform the action.
   * 
   * @param tree The tree on which the action must be performed.
   * @throws E
   */
  //  protected abstract void doPerform(TreeStructure<?> tree) throws E;

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
  public abstract <X extends T> void traverse(TreeStructure<X> tree) throws E;

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
  //  public abstract <X extends T, F extends Exception> void traverse(TreeStructure<X> tree, Guard<? super E, ? extends F> handler) throws F;

  /**
   * This method is called when a tree action arrives at a node. Code
   * that must be executed before {@link #perform(Object)} must be
   * in this method.
   * 
   * This method performs the type check and then invokes {@link #doEnter(Object)}.
   *
   * @param node The data structure node that has just been entered.
   */
  public default void enter(TreeStructure<?> node) throws E {
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
  public default void exit(TreeStructure<?> node) throws E {
  }
}
