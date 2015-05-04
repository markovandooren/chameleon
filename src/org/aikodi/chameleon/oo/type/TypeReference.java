package org.aikodi.chameleon.oo.type;

import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.reference.CrossReference;

/**
 * A class of cross-reference to types. 
 * 
 * @author Marko van Dooren
 */
public interface TypeReference extends CrossReference<Type> {

	public Type getType() throws LookupException;
	
	@Override
   public Type getElement() throws LookupException;
	
//	public TypeReference clone();

  public default TypeReference intersection(TypeReference other) {
		return other.intersectionDoubleDispatch(this);
	}

	
	public TypeReference intersectionDoubleDispatch(TypeReference other);
	
	public default TypeReference intersectionDoubleDispatch(IntersectionTypeReference other) {
		return other.intersectionDoubleDispatch(this);
	}
}
