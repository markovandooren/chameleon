package chameleon.oo.type;

import chameleon.core.element.Element;
import chameleon.core.namespace.NamespaceElementImpl;

/**
 * @author marko
 */
public abstract class StubTypeElement<E extends StubTypeElement> extends NamespaceElementImpl<E,Element> {

  public StubTypeElement(Element parent) {
    setUniParent(parent);
  }
  
}
