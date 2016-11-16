/**
 * 
 */
package org.aikodi.chameleon.util.action;

import org.aikodi.contract.Contracts;
import org.aikodi.rejuse.exception.Handler;
import org.aikodi.rejuse.tree.TreeStructure;

/**
 * @author Marko van Dooren
 *
 */
public class GuardedTreeWalker<T, I extends Exception, O extends Exception> implements TreeWalker<T, O> {

  private final TreeWalker<T, I> _wrapped;
  private final Handler<? super I,O> _handler;
  
  
  public GuardedTreeWalker(TreeWalker<T, I> wrapped, Handler<? super I,O> handler) {
    Contracts.notNull(wrapped, handler);
    this._wrapped = wrapped;
    this._handler = handler;
  }



  /**
   * @{inheritDoc}
   */
  @Override
  public <X extends T> void traverse(TreeStructure<X> tree) throws O {
    _handler.execute(() -> {_wrapped.traverse(tree);});
  }
  
  @Override
  public void exit(TreeStructure<?> tree) throws O {
  	_handler.execute(() -> {_wrapped.exit(tree);});
  }

  @Override
  public void enter(TreeStructure<?> tree) throws O {
  	_handler.execute(() -> {_wrapped.enter(tree);});
  }

}
