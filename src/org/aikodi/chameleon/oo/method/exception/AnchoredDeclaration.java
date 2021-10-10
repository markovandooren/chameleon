package org.aikodi.chameleon.oo.method.exception;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.rejuse.association.SingleAssociation;

import java.util.Set;

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
