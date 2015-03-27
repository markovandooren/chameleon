package org.aikodi.chameleon.core.event.association;

import org.aikodi.chameleon.core.element.Element;

/**
 * An event to signal that an element was replaced by another element.
 * 
 * @author Marko van Dooren
 */
public interface Replaced {
  
  /**
   * @return The element that was removed.
   */
  public Element removedElement();
  
  /**
   * @return The replacement element.
   */
  public Element addedElement();

}
