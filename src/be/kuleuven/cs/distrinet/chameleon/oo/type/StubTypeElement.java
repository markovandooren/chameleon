package be.kuleuven.cs.distrinet.chameleon.oo.type;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.element.ElementImpl;

/**
 * @author marko
 */
public abstract class StubTypeElement extends ElementImpl {

  public StubTypeElement(Element parent) {
    setUniParent(parent);
  }
  
}
