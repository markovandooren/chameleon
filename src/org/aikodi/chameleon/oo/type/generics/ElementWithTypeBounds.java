package org.aikodi.chameleon.oo.type.generics;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.type.Type;

public interface ElementWithTypeBounds extends Element {

  public abstract Type upperBound() throws LookupException;
  
  public abstract Type lowerBound() throws LookupException;


}
