package org.aikodi.chameleon.oo.type;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.element.ElementImpl;

/**
 * @author marko
 */
public abstract class StubTypeElement extends ElementImpl {

  public StubTypeElement(Element parent) {
    setUniParent(parent);
  }
  
}
