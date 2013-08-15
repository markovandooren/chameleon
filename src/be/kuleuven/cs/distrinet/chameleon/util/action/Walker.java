package be.kuleuven.cs.distrinet.chameleon.util.action;

import be.kuleuven.cs.distrinet.rejuse.action.Action;

public abstract class Walker<T, E extends Exception> extends Action<T,E> {

	public Walker(Class<T> type) {
		super(type);
	}

	public void enter(Object object) {
		if(type().isInstance(object)) {
			doEnter((T)object);
		}
	}
	
	/**
	 * Invoked when entering a particular object in the data structure.
	 * The default implementation does nothing.
	 * @param object
	 */
	public void doEnter(T object) {
	}
	
	public void exit(Object object) {
		if(type().isInstance(object)) {
			doExit((T)object);
		}
	}
	
	/**
	 * Invoked when entering a particular object in the data structure.
	 * The default implementation does nothing.
	 * @param object
	 */
	public void doExit(T object) {
	}
}
