package chameleon.core.reference;

import chameleon.core.MetamodelException;
import chameleon.core.declaration.Declaration;
import chameleon.core.element.Element;

/**
 * This interface represents the concept of a cross references in the model. For proper
 * functioning, every cross reference (method call, variable reference, type reference,...) must
 * implement this interface. 
 * 
 * @author Marko van Dooren
 * @author Tim Vermeiren
 */
public interface CrossReference<E extends CrossReference, P extends Element> extends Element<E,P> {
	
	public Declaration getElement() throws MetamodelException ;
	
}
