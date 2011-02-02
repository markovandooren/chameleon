package chameleon.oo.type;

import chameleon.core.element.Element;
import chameleon.core.namespace.NamespaceElementImpl;

/**
 * @author marko
 */
public abstract class StubTypeElement<E extends StubTypeElement> extends NamespaceElementImpl<E> {

  public StubTypeElement(Element parent) {
    setUniParent(parent);
  }
  
}
