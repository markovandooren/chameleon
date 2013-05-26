package be.kuleuven.cs.distrinet.chameleon.oo.expression;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;

/**
 * A class of elements to which expressions can be assigned.
 * 
 * @author Marko van Dooren
 */

public interface Assignable extends Element {

	public Type getType() throws LookupException;

}
