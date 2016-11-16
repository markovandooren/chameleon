package org.aikodi.chameleon.util.action;

import org.aikodi.rejuse.tree.TreeStructure;

/**
 * A tree walker that applies two walkers in sequence to a tree node.
 * 
 * @author Marko van Dooren
 *
 * @param <T> The type of the elements in the tree.
 * @param <E> The type of exceptions that can be thrown during the traversal.
 */
public class Sequence<T,E extends Exception> implements TreeWalker<T,E> {
	
	public Sequence(TreeWalker<T, ? extends E> first, TreeWalker<T, ? extends E> second) {
		_first = first;
		_second = second;
	}

	private TreeWalker<T, ? extends E> _first;
	
	/**
	 * @return The walker that is applied first to a node.
	 */
	public TreeWalker<T, ? extends E> first() {
	  return _first;
	}

	/**
	 * Set the first walker.
	 * 
	 * @param first The walker that will be applied first to a node.
	 */
	protected void setFirst(TreeWalker<T, ? extends E> first) {
		_first = first;
	}

  /**
   * @return The walker that is applied second to a node.
   */
	public TreeWalker<T, ? extends E> second() {
		return _second;
	}

  /**
   * Set the second walker.
   * 
   * @param second The walker that will be applied second to a node.
   */
	protected void setSecond(TreeWalker<T, ? extends E> second) {
		_second = second;
	}

	private TreeWalker<T, ? extends E> _second;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <X extends T> void traverse(TreeStructure<X> element) throws E {
		first().enter(element);
		first().traverse(element);
		second().enter(element);
		second().traverse(element);
		second().exit(element);
		first().exit(element);
	}
	
}
