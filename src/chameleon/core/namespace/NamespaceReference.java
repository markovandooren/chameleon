package chameleon.core.namespace;

import chameleon.core.declaration.TargetDeclaration;
import chameleon.core.reference.ElementReference;
import chameleon.core.reference.SpecificReference;
import chameleon.util.Util;

/**
 * @author marko
 */
public class NamespaceReference extends SpecificReference<NamespaceReference,Namespace> {

  public NamespaceReference(ElementReference<?, ? extends TargetDeclaration> target, String name) {
    super(target, name, Namespace.class);
  }
  
  public NamespaceReference(String qn) {
    super(getTarget(Util.getAllButLastPart(qn)), Util.getLastPart(qn), Namespace.class);
  }

  protected static NamespaceReference getTarget(String qn) {
    if(qn == null) {
      return null;
    }
    NamespaceReference target = new NamespaceReference(Util.getFirstPart(qn));
    qn = Util.getSecondPart(qn);
    while(qn != null) {
      NamespaceReference newTarget = new NamespaceReference(null, Util.getFirstPart(qn));
      newTarget.setTarget(target);
      target = newTarget;
      qn = Util.getSecondPart(qn);
    }
    return target;
  }
  
  public NamespaceReference clone() {
    return new NamespaceReference((getTarget() == null ? null : getTarget().clone()),getName());
  }

  
}
