package be.kuleuven.cs.distrinet.chameleon.util.action;

import be.kuleuven.cs.distrinet.rejuse.action.Action;

public abstract class Walker<T, E extends Exception> extends Action<T,E> {

	public Walker(Class<T> type) {
		super(type);
	}

	/**
	 * Invoked when entering a particular object in the data structure.
	 * The default implementation does nothing.
	 * @param object
	 */
	public void enter(T object) {
	}
	
	/**
	 * Invoked when entering a particular object in the data structure.
	 * The default implementation does nothing.
	 * @param object
	 */
	public void exit(T object) {
	}
}
