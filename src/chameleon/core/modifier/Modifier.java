package chameleon.core.modifier;

import org.rejuse.property.PropertySet;

import chameleon.core.element.Element;


/**
 * @author Marko van Dooren
 */
public interface Modifier<E extends Modifier, P extends Element> extends Element<E,P> {

 /*@
   @ behavior
   @
   @ post \result != null; 
   @*/
  public PropertySet<Element> impliedProperties();
  
  /**
   * Return a clone of this modifier.
   * @return
   */
  public E clone();

}
