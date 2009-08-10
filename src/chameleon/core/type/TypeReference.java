package chameleon.core.type;

import chameleon.core.declaration.TargetDeclaration;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.reference.ElementReference;
import chameleon.core.reference.SpecificReference;

/**
 * @author Marko van Dooren
 */
public class TypeReference extends SpecificReference<TypeReference,Element,Type> {

  public TypeReference(String qn) {
    super(qn, Type.class);
  }
  
  public TypeReference(ElementReference<?, ?, ? extends TargetDeclaration> target, String name) {
    super(target, name, Type.class);
  }
  
  public Type getType() throws LookupException {
  	return getElement();
  }

  public TypeReference clone() {
    return new TypeReference((getTarget() == null ? null : getTarget().clone()),getName());
  }
  
}
