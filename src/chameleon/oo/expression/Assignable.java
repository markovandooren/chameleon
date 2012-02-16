package chameleon.oo.expression;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.oo.type.Type;

/**
 * A class of elements to which expressions can be assigned.
 * 
 * @author Marko van Dooren
 */

public interface Assignable extends Element {

	public Type getType() throws LookupException;

  public Assignable clone();
}
