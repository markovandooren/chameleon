package chameleon.core.type;

import java.util.List;

import org.rejuse.association.OrderedReferenceSet;
import org.rejuse.logic.ternary.Ternary;
import org.rejuse.property.Property;
import org.rejuse.property.PropertySet;

import chameleon.core.element.Element;
import chameleon.core.modifier.Modifier;
import chameleon.core.namespacepart.NamespacePartElementImpl;

/**
 * Support class for member-like elements that can be the direct children of a type.
 * 
 * @author Marko van Dooren
 *
 * @param <E> The type of the element
 * @param <P> The type of the parent
 */
public abstract class TypeElementImpl<E extends TypeElementImpl<E,P>, P extends Element> extends NamespacePartElementImpl<E, P> implements TypeElement<E,P> {
  
  /*************
   * MODIFIERS *
   *************/
  private OrderedReferenceSet<TypeElement, Modifier> _modifiers = new OrderedReferenceSet<TypeElement, Modifier>(this);

  /**
   * Return the list of modifiers of this member.
   */
 /*@
   @ behavior
   @
   @ post \result != null;
   @*/
  public List<Modifier> modifiers() {
    return _modifiers.getOtherEnds();
  }

  public void addModifier(Modifier modifier) {
    if ((modifier != null) && (!_modifiers.contains(modifier.parentLink()))) {
      _modifiers.add(modifier.parentLink());
    }
  }

  public void removeModifier(Modifier modifier) {
    _modifiers.remove(modifier.parentLink());
  }

  public boolean hasModifier(Modifier modifier) {
    return _modifiers.getOtherEnds().contains(modifier);
  }

  public PropertySet<Element> declaredProperties() {
    return myDeclaredProperties();
  }

	protected PropertySet<Element> myDeclaredProperties() {
		PropertySet<Element> result = new PropertySet<Element>();
    for(Modifier modifier:modifiers()) {
      result.addAll(modifier.impliedProperties().properties());
    }
    return result;
	}

	protected PropertySet<Element> myDefaultProperties() {
		return language().defaultProperties(this);
	}

}
