package org.aikodi.chameleon.core.event.association;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.event.Change;

/**
 * An event to signal that an element was added. This does not cover
 * replacement of elements, only pure addition.
 * 
 * @author Marko van Dooren
 */
public interface Added extends Change {
  
  /**
   * @return The element that was added.
   */
  public Element element();

}
