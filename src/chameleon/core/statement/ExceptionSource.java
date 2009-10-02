package chameleon.core.statement;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;

/**
 * @author Marko van Dooren
 */

public interface ExceptionSource<E extends ExceptionSource, P extends Element> extends Element<E, P> {

	/**
	 *  
	 */
	public CheckedExceptionList getCEL() throws LookupException;

	/**
	 * 
	 * @uml.property name="absCEL"
	 * @uml.associationEnd 
	 * @uml.property name="absCEL" multiplicity="(0 1)"
	 */
	public CheckedExceptionList getAbsCEL() throws LookupException;

}
