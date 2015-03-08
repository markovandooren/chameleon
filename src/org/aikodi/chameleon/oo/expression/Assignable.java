package org.aikodi.chameleon.oo.expression;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.type.Type;

/**
 * A class of elements to which expressions can be assigned.
 * 
 * @author Marko van Dooren
 */

public interface Assignable extends Element {

	public Type getType() throws LookupException;

}
