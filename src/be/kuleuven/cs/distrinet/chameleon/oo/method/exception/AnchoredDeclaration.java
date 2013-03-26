package be.kuleuven.cs.distrinet.chameleon.oo.method.exception;

import java.util.Set;

import be.kuleuven.cs.distrinet.rejuse.association.SingleAssociation;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;

/**
 * @author Marko van Dooren
 */
public interface AnchoredDeclaration extends Element {
	public Set getRawExceptionTypes() throws LookupException;

	/**
	 * @return
	 */
	public SingleAssociation getFilterClauseLink();

	/**
	 * @return
	 */
	//public Reference getParentLink();
}
