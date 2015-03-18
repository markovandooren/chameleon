package org.aikodi.chameleon.util.action;

import be.kuleuven.cs.distrinet.rejuse.tree.TreeStructure;

/**
 * A walker that applies the given walker to the children of a tree node.
 * 
 * @author Marko van Dooren
 *
 * @param <T> 
 * @param <E>
 */
public class Recurse<T, E extends Exception> extends TreeWalker<T, E> {

  public Recurse(TreeWalker<T, ? extends E> walker) {
    _action = walker;
  }

  public TreeWalker<T, ? extends E> walker() {
    return _action;
  }

  private TreeWalker<T, ? extends E> _action;

  @Override
  public <X extends T> void traverse(TreeStructure<X> tree) throws E {
    for (TreeStructure<? extends X> child : tree.branches()) {
      walker().enter(child);
      walker().traverse(child);
      walker().exit(child);
    }
  }

}
