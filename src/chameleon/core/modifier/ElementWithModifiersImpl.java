package chameleon.core.modifier;

import java.util.List;

import org.rejuse.association.OrderedMultiAssociation;
import org.rejuse.property.PropertySet;

import chameleon.core.element.Element;
import chameleon.core.namespace.NamespaceElementImpl;
import chameleon.core.property.ChameleonProperty;
import chameleon.exception.ChameleonProgrammerException;

public abstract class ElementWithModifiersImpl<E extends Element<E, P>, P extends Element> extends NamespaceElementImpl<E,P> {

  /*************
   * MODIFIERS *
   *************/
  private OrderedMultiAssociation<ElementWithModifiersImpl<E,P>, Modifier> _modifiers = new OrderedMultiAssociation<ElementWithModifiersImpl<E,P>, Modifier>(this);

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

  public List<Element> children() {
  	return (List)modifiers();
  }
  
  public void addModifier(Modifier modifier) {
    if (modifier != null) {
    	if (!_modifiers.contains(modifier.parentLink())) {
    		_modifiers.add(modifier.parentLink());	
      }
    } else {
    	throw new ChameleonProgrammerException("Modifier passed to addModifier is null");
    }
  }
  
  public void addModifiers(List<Modifier> modifiers) {
  	if(modifiers == null) {
  		throw new ChameleonProgrammerException("List passed to addModifiers is null");
  	} else {
  		for(Modifier modifier: modifiers) {
  			addModifier(modifier);
  		}
  	}
  }

  public void removeModifier(Modifier modifier) {
  	if(modifier != null) {
      _modifiers.remove(modifier.parentLink());
  	} else {
  		throw new ChameleonProgrammerException("Modifier passed to removeModifier was null");
  	}
  }

  public boolean hasModifier(Modifier modifier) {
    return _modifiers.getOtherEnds().contains(modifier);
  }

  public PropertySet<Element,ChameleonProperty> declaredProperties() {
    return myDeclaredProperties();
  }

	protected PropertySet<Element,ChameleonProperty> myDeclaredProperties() {
		PropertySet<Element,ChameleonProperty> result = new PropertySet<Element,ChameleonProperty>();
    for(Modifier modifier:modifiers()) {
      result.addAll(modifier.impliedProperties().properties());
    }
    return result;
	}

	/**
	 * Return the default properties for this element.
	 * @return
	 */
	protected PropertySet<Element,ChameleonProperty> myDefaultProperties() {
		return language().defaultProperties(this);
	}

}
