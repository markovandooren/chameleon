package chameleon.oo.type;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.lookup.LookupException;
import chameleon.core.reference.CrossReferenceTarget;
import chameleon.core.reference.SpecificReference;
import chameleon.oo.language.ObjectOrientedLanguage;

/**
 * @author Marko van Dooren
 */
public class BasicTypeReference<E extends BasicTypeReference> extends SpecificReference<Type> implements TypeReference {

  public BasicTypeReference(String fqn) {
    super(fqn, Type.class);
  }
  
  public BasicTypeReference(CrossReferenceTarget target, String name) {
    super(target, name, Type.class);
  }
  
  public BasicTypeReference(CrossReferenceTarget target, SimpleNameSignature signature) {
    super(target, signature, Type.class);
  }
  
  public Type getType() throws LookupException {
  	return getElement();
  }

  public E clone() {
    return (E) new BasicTypeReference((getTarget() == null ? null : getTarget().clone()),(SimpleNameSignature)signature().clone());
  }

	public TypeReference intersection(TypeReference other) {
		return other.intersectionDoubleDispatch(this);
	}

	public TypeReference intersectionDoubleDispatch(TypeReference other) {
		return language(ObjectOrientedLanguage.class).createIntersectionReference(clone(), other.clone());
	}

	public TypeReference intersectionDoubleDispatch(IntersectionTypeReference other) {
		IntersectionTypeReference result = other.clone();
		result.add(clone());
		return result;
	}

	@Override
	public String toString() {
		String result;
		try {
			try {
				result = getElement().infoDisplayName();
			} catch (LookupException e) {
				result = toString();
			}
		}	catch(Exception exc) {
			result = "";
		}
		return result;
	}
  
}
