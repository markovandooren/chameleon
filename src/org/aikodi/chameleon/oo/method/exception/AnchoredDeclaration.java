package org.aikodi.chameleon.oo.method.exception;

import java.util.Set;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;

import be.kuleuven.cs.distrinet.rejuse.association.SingleAssociation;

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
