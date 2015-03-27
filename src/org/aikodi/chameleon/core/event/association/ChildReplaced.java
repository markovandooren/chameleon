package org.aikodi.chameleon.core.event;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.contract.Contracts;

/**
 * An event that indicates that an element was replaced by another element.
 * 
 * @author Marko van Dooren
 */
public class Replaced extends Change {

  private Element _oldElement;
  
  private Element _newElement;

  /**
   * Create a new event to indicate that the given old element was
   * replaced by the given new element.
   * 
   * @param oldElement The element that was replaced.
   *                   The element cannot be null.
   * @param newElement The replacement element.
   *                   The element cannot be null.
   */
  public Replaced(Element oldElement, Element newElement) {
    Contracts.notNull(oldElement, "The replaced element cannot be null.");
    Contracts.notNull(newElement, "The replacement element cannot be null.");
    this._oldElement = oldElement;
    this._newElement = newElement;
  }
  
  /**
   * @return The element that was replaced.
   */
  public Element oldElement() {
    return _oldElement;
  }
  
  /**
   * @return The replacement element.
   */
  public Element newElement() {
    return _newElement;
  }
}
