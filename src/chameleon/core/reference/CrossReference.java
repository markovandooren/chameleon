package chameleon.core.reference;

import chameleon.core.declaration.Declaration;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;

/**
 * This interface represents the concept of a cross references in the model. For proper
 * functioning, every cross reference (method call, variable reference, type reference,...) must
 * implement this interface. 
 * 
 * @author Marko van Dooren
 * @author Tim Vermeiren
 * 
 * <D> The type of the declaration that is referenced by this cross reference.
 */
public interface CrossReference<E extends CrossReference, P extends Element, D extends Declaration> extends Element<E,P> {
	
	public D getElement() throws LookupException;
	
	public Declaration getDeclarator() throws LookupException;
	
	public DeclarationSelector<D> selector();
}
