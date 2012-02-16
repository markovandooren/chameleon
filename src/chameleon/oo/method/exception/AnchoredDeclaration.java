package chameleon.oo.method.exception;

import java.util.Set;

import org.rejuse.association.SingleAssociation;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;

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
