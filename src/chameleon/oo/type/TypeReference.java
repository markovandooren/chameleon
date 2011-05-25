package chameleon.oo.type;

import chameleon.core.lookup.LookupException;
import chameleon.core.reference.CrossReference;

/**
 * @author Marko van Dooren
 */
public interface TypeReference<E extends TypeReference> extends CrossReference<E,Type> {

	public Type getType() throws LookupException;
	
	public String infoDisplayName();
	
	public Type getElement() throws LookupException;
	
	public E clone();

	public TypeReference intersection(TypeReference other);
	
	public TypeReference intersectionDoubleDispatch(TypeReference other);
	
	public TypeReference intersectionDoubleDispatch(IntersectionTypeReference<?> other);
}
