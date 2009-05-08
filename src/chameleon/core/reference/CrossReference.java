package chameleon.core.reference;

import chameleon.core.MetamodelException;
import chameleon.core.declaration.Declaration;
import chameleon.core.element.Element;

/**
 * @author Marko van Dooren
 * @author Tim Vermeiren
 */
public interface CrossReference<E extends CrossReference, P extends Element> extends Element<E,P> {
	
	public Declaration getElement() throws MetamodelException ;
	
}
