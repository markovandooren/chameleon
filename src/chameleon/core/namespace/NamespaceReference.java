package chameleon.core.namespace;

import chameleon.core.declaration.TargetDeclaration;
import chameleon.core.element.Element;
import chameleon.core.reference.ElementReference;
import chameleon.core.reference.SpecificReference;
import chameleon.util.Util;

/**
 * @author marko
 */
public class NamespaceReference extends SpecificReference<NamespaceReference,Element,Namespace> {

  public NamespaceReference(ElementReference<?, ?, ? extends TargetDeclaration> target, String name) {
    super(target, name, Namespace.class);
  }
  
  public NamespaceReference(String qn) {
    super(qn, Namespace.class);
  }

  public NamespaceReference clone() {
    return new NamespaceReference((getTarget() == null ? null : getTarget().clone()),getName());
  }

  
}
