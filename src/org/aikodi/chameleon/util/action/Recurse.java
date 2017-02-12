package org.aikodi.chameleon.util.action;

import org.aikodi.rejuse.tree.TreeStructure;

/**
 * A walker that applies the given walker to the children of a tree node.
 * 
 * @author Marko van Dooren
 *
 * @param <T> 
 * @param <E>
 */
public class Recurse<T, E extends Exception> implements TreeWalker<T, E> {

  public Recurse(TreeWalker<T, ? extends E> walker) {
    _action = walker;
  }

  public TreeWalker<T, ? extends E> walker() {
    return _action;
  }

  private TreeWalker<T, ? extends E> _action;

  @Override
  public <X extends T, N extends Exception> void traverse(TreeStructure<X, N> tree) throws E, N {
    for (TreeStructure<? extends X, N> child : tree.branches()) {
      walker().enter(child);
      walker().traverse(child);
      walker().exit(child);
    }
  }

}
