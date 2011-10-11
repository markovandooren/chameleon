package chameleon.oo.method.exception;

import java.util.Set;

import org.rejuse.association.SingleAssociation;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;

/**
 * @author marko
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface AnchoredDeclaration<E extends Element> extends Element<E> {
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
