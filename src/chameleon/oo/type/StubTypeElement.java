package chameleon.oo.type;

import chameleon.core.element.Element;
import chameleon.core.element.ElementImpl;

/**
 * @author marko
 */
public abstract class StubTypeElement extends ElementImpl {

  public StubTypeElement(Element parent) {
    setUniParent(parent);
  }
  
}
