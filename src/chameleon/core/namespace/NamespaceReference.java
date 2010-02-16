package chameleon.core.namespace;

import chameleon.core.declaration.Signature;
import chameleon.core.declaration.TargetDeclaration;
import chameleon.core.element.Element;
import chameleon.core.reference.CrossReference;
import chameleon.core.reference.SpecificReference;

/**
 * @author marko
 */
public class NamespaceReference extends SpecificReference<NamespaceReference,Element,Namespace> {

  public NamespaceReference(CrossReference<?, ?, ? extends TargetDeclaration> target, String name) {
    super(target, name, Namespace.class);
  }
  
  public NamespaceReference(CrossReference<?, ?, ? extends TargetDeclaration> target, Signature signature) {
    super(target, signature, Namespace.class);
  }
  
  public NamespaceReference(String qn) {
    super(qn, Namespace.class);
  }

  public NamespaceReference clone() {
    return new NamespaceReference((getTarget() == null ? null : getTarget().clone()),signature().clone());
  }

  
}
