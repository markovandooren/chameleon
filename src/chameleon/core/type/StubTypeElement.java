package chameleon.core.type;

import chameleon.core.element.Element;
import chameleon.core.lookup.LexicalLookupStrategy;
import chameleon.core.namespacepart.NamespaceElementImpl;

/**
 * @author marko
 */
public abstract class StubTypeElement<E extends StubTypeElement> extends NamespaceElementImpl<E,Element> {

  public StubTypeElement(Element parent) {
    setUniParent(parent);
  }
  
}
