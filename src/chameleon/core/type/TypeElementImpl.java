package chameleon.core.type;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.modifier.ElementWithModifiersImpl;
import chameleon.core.statement.CheckedExceptionList;

/**
 * Support class for member-like elements that can be the direct children of a type.
 * 
 * @author Marko van Dooren
 *
 * @param <E> The type of the element
 * @param <P> The type of the parent
 */
public abstract class TypeElementImpl<E extends TypeElementImpl<E,P>, P extends Element> extends ElementWithModifiersImpl<E, P> implements TypeElement<E,P> {
  

	public CheckedExceptionList getCEL() throws LookupException {
	  return new CheckedExceptionList();	
	}
	
	public CheckedExceptionList getAbsCEL() throws LookupException {
		return new CheckedExceptionList();
	}

	
	
}
