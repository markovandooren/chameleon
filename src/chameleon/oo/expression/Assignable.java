package chameleon.oo.expression;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.oo.type.Type;

/**
 * A class of elements to which expressions can be assigned.
 * 
 * @author Marko van Dooren
 */

public interface Assignable<E extends Assignable> extends Element<E> {

	public Type getType() throws LookupException;

  public E clone();
}
