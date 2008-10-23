/*
 * Created on Mar 7, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package chameleon.core.method.exception;

import java.util.Set;

import org.rejuse.association.Reference;

import chameleon.core.MetamodelException;
import chameleon.core.element.Element;
import chameleon.core.type.TypeDescendant;

/**
 * @author marko
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface AnchoredDeclaration<E extends Element, P extends Element> extends TypeDescendant<E,P> {
	public Set getRawExceptionTypes() throws MetamodelException;

	/**
	 * @return
	 */
	public Reference getFilterClauseLink();

	/**
	 * @return
	 */
	//public Reference getParentLink();
}
