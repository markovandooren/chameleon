package org.aikodi.chameleon.util.action;

import org.aikodi.rejuse.action.UniversalConsumer;
import org.aikodi.rejuse.tree.TreeStructure;


public class Apply<T,E extends Exception> implements TreeWalker<T, E> {

  public Apply(UniversalConsumer<T, E> action) {
    if(action == null) {
      throw new IllegalArgumentException("The action to apply cannot be null.");
    }
    this._action = action;
  }

  private UniversalConsumer<T,E> _action;
  
  public UniversalConsumer<T,E> action() {
    return _action;
  }
  
  @Override
  public <X extends T, N extends Exception> void traverse(TreeStructure<X, N> tree) throws E {
    action().perform(tree.node());
  }

}
