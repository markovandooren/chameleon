package be.kuleuven.cs.distrinet.chameleon.core.namespace;

import be.kuleuven.cs.distrinet.chameleon.core.reference.CrossReferenceTarget;
import be.kuleuven.cs.distrinet.chameleon.core.reference.SimpleReference;

/**
 * @author Marko van Dooren
 */
public class NamespaceReference extends SimpleReference<Namespace> {
	

  public NamespaceReference(CrossReferenceTarget target, String name) {
    super(target, name, Namespace.class);
  }
  
  public NamespaceReference(String qn) {
    super(qn, Namespace.class);
  }

  protected NamespaceReference cloneSelf() {
    return new NamespaceReference(null,name());
  }

  
}
