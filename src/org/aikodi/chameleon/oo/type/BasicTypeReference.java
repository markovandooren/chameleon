package org.aikodi.chameleon.oo.type;

import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.reference.CrossReferenceTarget;
import org.aikodi.chameleon.core.reference.NameReference;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;

/**
 * @author Marko van Dooren
 */
public class BasicTypeReference extends NameReference<Type> implements TypeReference {

  public BasicTypeReference(String fqn) {
    super(fqn, Type.class);
  }
  
  public BasicTypeReference(CrossReferenceTarget target, String name) {
    super(target, name, Type.class);
  }
  
  @Override
public Type getType() throws LookupException {
  	return getElement();
  }

  @Override
public BasicTypeReference cloneSelf() {
    return new BasicTypeReference(null,name());
  }

	@Override
   public TypeReference intersection(TypeReference other) {
		return other.intersectionDoubleDispatch(this);
	}

	@Override
   public TypeReference intersectionDoubleDispatch(TypeReference other) {
		return language(ObjectOrientedLanguage.class).createIntersectionReference(clone(this), clone(other));
	}

	@Override
   public TypeReference intersectionDoubleDispatch(IntersectionTypeReference other) {
		IntersectionTypeReference result = clone(other);
		result.add(clone(this));
		return result;
	}

//	@Override
//	public String toString() {
//		String result;
//		try {
//				result = getElement().toString();
//		}	catch(Exception exc) {
//			result = "";
//		}
//		return result;
//	}
  
}
