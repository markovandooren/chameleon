package org.aikodi.chameleon.oo.type;

import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;

/**
 * A class of cross-reference to types. 
 * 
 * @author Marko van Dooren
 */
public interface TypeReference extends CrossReference<Type> {

	@Override
   public Type getElement() throws LookupException;
	
  public default TypeReference intersection(TypeReference other) {
		return other.intersectionDoubleDispatch(this);
	}

	
  public default TypeReference intersectionDoubleDispatch(TypeReference other) {
    return language(ObjectOrientedLanguage.class).createIntersectionReference(clone(this), clone(other));
  }

	
	public default TypeReference intersectionDoubleDispatch(IntersectionTypeReference other) {
		return other.intersectionDoubleDispatch(this);
	}
}
