package chameleon.core.type;

import chameleon.core.declaration.TargetDeclaration;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.reference.CrossReference;
import chameleon.core.reference.SpecificReference;

/**
 * @author Marko van Dooren
 */
public class TypeReference extends SpecificReference<TypeReference,Element,Type> {

  public TypeReference(String fqn) {
    super(fqn, Type.class);
  }
  
  public TypeReference(CrossReference<?, ?, ? extends TargetDeclaration> target, String name) {
    super(target, name, Type.class);
  }
  
  public Type getType() throws LookupException {
  	return getElement();
  }

  public TypeReference clone() {
    return new TypeReference((getTarget() == null ? null : getTarget().clone()),getName());
  }
  
}
