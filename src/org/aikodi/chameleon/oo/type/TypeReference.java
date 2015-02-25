package org.aikodi.chameleon.oo.type;

import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.reference.CrossReference;

/**
 * @author Marko van Dooren
 */
public interface TypeReference extends CrossReference<Type> {

	public Type getType() throws LookupException;
	
	@Override
   public Type getElement() throws LookupException;
	
//	public TypeReference clone();

	public TypeReference intersection(TypeReference other);
	
	public TypeReference intersectionDoubleDispatch(TypeReference other);
	
	public TypeReference intersectionDoubleDispatch(IntersectionTypeReference other);
}
