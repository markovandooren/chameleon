package chameleon.core.reference;

import chameleon.core.MetamodelException;
import chameleon.core.declaration.Declaration;
import chameleon.core.namespacepart.NamespacePartElement;

/**
 * @author Marko van Dooren
 * @author Tim Vermeiren
 */
public interface CrossReference {
	
	public Declaration getElement() throws MetamodelException ;
	
}
