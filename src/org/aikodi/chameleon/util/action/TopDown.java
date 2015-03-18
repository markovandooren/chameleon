package org.aikodi.chameleon.util.action;

/**
 * A walker for top down tree traversal.
 * 
 * @author Marko van Dooren
 *
 * @param <T> The type of the elements in the tree.
 * @param <E> The type of exceptions that can be thrown during the traversal.
 */
public class TopDown<T,E extends Exception> extends Sequence<T,E> {

	public TopDown(TreeWalker<T, ? extends E> walker) {
		super(walker, null);
		setSecond(new Recurse<T,E>(this));
	}

}
