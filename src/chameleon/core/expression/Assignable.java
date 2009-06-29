package chameleon.core.expression;

import chameleon.core.context.LookupException;
import chameleon.core.element.Element;
import chameleon.core.type.Type;

/**
 * A class of elements to which expressions can be assigned.
 * 
 * @author Marko van Dooren
 */

public interface Assignable<E extends Assignable, P extends Element> extends Element<E,P> {

	public Type getType() throws LookupException;

  public E clone();
}
