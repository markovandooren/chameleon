package be.kuleuven.cs.distrinet.chameleon.oo.statement;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;

/**
 * @author Marko van Dooren
 */

public interface ExceptionSource extends Element {

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
