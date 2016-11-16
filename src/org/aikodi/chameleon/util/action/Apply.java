package org.aikodi.chameleon.util.action;

import org.aikodi.rejuse.action.Action;
import org.aikodi.rejuse.tree.TreeStructure;


public class Apply<T,E extends Exception> implements TreeWalker<T, E> {

  public Apply(Action<T, E> action) {
    if(action == null) {
      throw new IllegalArgumentException("The action to apply cannot be null.");
    }
    this._action = action;
  }

  private Action<T,E> _action;
  
  public Action<T,E> action() {
    return _action;
  }
  
  @Override
  public <X extends T> void traverse(TreeStructure<X> tree) throws E {
    action().perform(tree.node());
  }

}
