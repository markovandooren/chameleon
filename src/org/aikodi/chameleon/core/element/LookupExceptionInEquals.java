package org.aikodi.chameleon.core.element;

import org.aikodi.chameleon.core.lookup.LookupException;

/**
 * An exception class to tunnel a {@link LookupException} through the
 * {@link #equals(Object)} method.
 * 
 * @author Marko van Dooren
 */
public class LookupExceptionInEquals extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6205482572328033947L;

	/**
	 * Create a new exception that wraps the given lookup exception.
	 * 
	 * @param message The message of the new exception
	 * @param cause The exception that was thrown in the equals method.
	 */
	public LookupExceptionInEquals(String message, LookupException cause) {
		super(message, cause);
	}

}
