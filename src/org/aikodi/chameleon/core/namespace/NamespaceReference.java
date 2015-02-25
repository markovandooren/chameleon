package org.aikodi.chameleon.core.namespace;

import org.aikodi.chameleon.core.reference.CrossReferenceTarget;
import org.aikodi.chameleon.core.reference.NameReference;

/**
 * @author Marko van Dooren
 */
public class NamespaceReference extends NameReference<Namespace> {
	

  public NamespaceReference(CrossReferenceTarget target, String name) {
    super(target, name, Namespace.class);
  }
  
  public NamespaceReference(String qn) {
    super(qn, Namespace.class);
  }

  @Override
protected NamespaceReference cloneSelf() {
    return new NamespaceReference(null,name());
  }

  
}
