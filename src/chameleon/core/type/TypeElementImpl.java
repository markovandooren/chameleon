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
 * Support class for type elements.
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
    if ((modifier != null) && (!_modifiers.contains(modifier.getParentLink()))) {
      _modifiers.add(modifier.getParentLink());
    }
  }

  public void removeModifier(Modifier modifier) {
    _modifiers.remove(modifier.getParentLink());
  }

  public boolean hasModifier(Modifier modifier) {
    return _modifiers.getOtherEnds().contains(modifier);
  }

//  public AccessModifier getAccessModifier() {
//    return _accessModifier;
//  }
//
//  public void setAccessModifier(AccessModifier access) {
//    if ((!_modifiers.contains(access.getParentLink())) && (access != null)) {
//      if(_accessModifier != null) {
//        _modifiers.remove(_accessModifier.getParentLink());
//      }
//      _modifiers.add(access.getParentLink());
//      _accessModifier = access;
//    }
//  }

  //@FIXME remove this and replace by modifier category or something similar
//  private AccessModifier _accessModifier;

  public Ternary is(Property<Element> property) {
    PropertySet<Element> declared = declaredProperties();
    if((property).appliesTo(this)) {
      declared.add(property);
    }
    return declared.implies(property);
  }
 
  public PropertySet<Element> declaredProperties() {
    PropertySet<Element> result = new PropertySet<Element>();
    for(Modifier modifier:modifiers()) {
      result.addAll(modifier.impliedProperties());
    }
    return result;
  }


}
