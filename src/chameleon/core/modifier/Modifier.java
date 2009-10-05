package chameleon.core.modifier;

import org.rejuse.property.PropertySet;

import chameleon.core.element.Element;
import chameleon.core.property.ChameleonProperty;


/**
 * @author Marko van Dooren
 */
public interface Modifier<E extends Modifier, P extends Element> extends Element<E,P> {

 /*@
   @ behavior
   @
   @ post \result != null; 
   @*/
  public PropertySet<Element,ChameleonProperty> impliedProperties();
  
  /**
   * Return a clone of this modifier.
   * @return
   */
  public E clone();

}
