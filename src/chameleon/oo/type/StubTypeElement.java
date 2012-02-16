package chameleon.oo.type;

import chameleon.core.element.Element;
import chameleon.core.namespace.NamespaceElementImpl;

/**
 * @author marko
 */
public abstract class StubTypeElement extends NamespaceElementImpl {

  public StubTypeElement(Element parent) {
    setUniParent(parent);
  }
  
}
