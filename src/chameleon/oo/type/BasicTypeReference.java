package chameleon.oo.type;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.declaration.TargetDeclaration;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.reference.CrossReference;
import chameleon.core.reference.SpecificReference;
import chameleon.oo.language.ObjectOrientedLanguage;

/**
 * @author Marko van Dooren
 */
public class BasicTypeReference<E extends BasicTypeReference> extends SpecificReference<E,Element,Type> implements TypeReference<E> {

  public BasicTypeReference(String fqn) {
    super(fqn, Type.class);
  }
  
  public BasicTypeReference(CrossReference<?, ?, ? extends TargetDeclaration> target, String name) {
    super(target, name, Type.class);
  }
  
  public BasicTypeReference(CrossReference<?, ?, ? extends TargetDeclaration> target, SimpleNameSignature signature) {
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

	public TypeReference intersectionDoubleDispatch(IntersectionTypeReference<?> other) {
		IntersectionTypeReference<?> result = other.clone();
		result.add(clone());
		return result;
	}
  
}
