package chameleon.oo.type;

import chameleon.core.lookup.LookupException;
import chameleon.core.reference.CrossReference;

/**
 * @author Marko van Dooren
 */
public interface TypeReference extends CrossReference<Type> {

	public Type getType() throws LookupException;
	
	public Type getElement() throws LookupException;
	
	public TypeReference clone();

	public TypeReference intersection(TypeReference other);
	
	public TypeReference intersectionDoubleDispatch(TypeReference other);
	
	public TypeReference intersectionDoubleDispatch(IntersectionTypeReference other);
}
