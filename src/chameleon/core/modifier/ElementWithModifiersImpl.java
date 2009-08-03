package chameleon.core.modifier;

import java.util.List;

import org.rejuse.association.OrderedReferenceSet;
import org.rejuse.property.PropertySet;

import chameleon.core.element.ChameleonProgrammerException;
import chameleon.core.element.Element;
import chameleon.core.namespace.NamespaceElementImpl;

public abstract class ElementWithModifiersImpl<E extends Element<E, P>, P extends Element> extends NamespaceElementImpl<E,P> {

  /*************
   * MODIFIERS *
   *************/
  private OrderedReferenceSet<ElementWithModifiersImpl<E,P>, Modifier> _modifiers = new OrderedReferenceSet<ElementWithModifiersImpl<E,P>, Modifier>(this);

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
