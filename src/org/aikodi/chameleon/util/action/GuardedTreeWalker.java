package org.aikodi.chameleon.util.action;

import org.aikodi.contract.Contracts;
import org.aikodi.rejuse.exception.Handler;
import org.aikodi.rejuse.exception.Handler.Executor;
import org.aikodi.rejuse.exception.Handler.ExecutorWithTwoExceptions;
import org.aikodi.rejuse.tree.TreeStructure;

/**
 * A ckass of treewalkers that use a handler to deal with exceptions.
 * 
 * @author Marko van Dooren
 * 
 * @param <T> The type of the elements in the tree.
 * @param <I> The type of exceptions that are thrown by the {@link #enter(TreeStructure)} and 
 *            {@link #exit(TreeStructure)} methods.
 * @param <O> The type of exceptions that can be signaled during tree            
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
   * @throws N 
   * @{inheritDoc}
   */
  @Override
  public <X extends T, N extends Exception> void traverse(TreeStructure<X, N> tree) throws O, N {
  	ExecutorWithTwoExceptions<I, N> executor = () -> _wrapped.traverse(tree); 
    _handler.execute(executor);
  }
  
  @Override
  public <N extends Exception> void exit(TreeStructure<?, N> tree) throws O {
  	Executor<I> executor = () -> _wrapped.exit(tree); 
  	_handler.execute(executor);
  }

  @Override
  public <N extends Exception> void enter(TreeStructure<?, N> tree) throws O {
  	Executor<I> executor = () -> _wrapped.enter(tree);
  	_handler.execute(executor);
  }

}
