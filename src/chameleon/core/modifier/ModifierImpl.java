package chameleon.core.modifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.rejuse.property.Property;
import org.rejuse.property.PropertySet;

import chameleon.core.element.Element;
import chameleon.core.element.ElementImpl;


/**
 * A convenience class for modifiers. Setting the parent is not done in the constructor
 * because adding a modifier to an element will set up the bidirectional association.
 * 
 * @author Marko van Dooren
 */
public abstract class ModifierImpl<E extends Modifier,P extends Element> extends ElementImpl<E,P> implements Modifier<E,P> {



  public ModifierImpl() {
  }
 
  /**
   * Clone this modifier.
   */
  public abstract E clone();

  /**
   * By default, a modifier has no children, but subclasses are allowed to
   * change this behavior. Therefore, you cannot rely on children() returning
   * an empty set.
   */
  public List<? extends Element> children() {
    return new ArrayList<Element>();
  }

  /**
   * Convenience method for creating an empty propertyset
   */
  protected PropertySet<Element> createSet() {
    return new PropertySet<Element>(); 
  }
  
  /**
   * Convenience method for creating a propertyset with a single element.
   */
  protected PropertySet<Element> createSet(Property p) {
    PropertySet<Element> result = createSet();
    result.add(p);
    return result;
  }

  /**
   * Convenience method for creating a propertyset with two elements.
   */
  protected PropertySet<Element> createSet(Property p1, Property p2) {
  	PropertySet<Element> result = createSet(p1);
    result.add(p2);
    return result;
  }
  
  /**
   * Convenience method for creating a propertyset with three elements.
   */
  protected PropertySet<Element> createSet(Property p1, Property p2, Property p3) {
  	PropertySet<Element> result = createSet(p1, p2);
    result.add(p3);
    return result;
  }
  
}
