package be.kuleuven.cs.distrinet.chameleon.core.modifier;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.element.ElementImpl;
import be.kuleuven.cs.distrinet.chameleon.core.property.ChameleonProperty;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.chameleon.exception.ModelException;
import be.kuleuven.cs.distrinet.chameleon.util.association.Multi;
import be.kuleuven.cs.distrinet.rejuse.property.Property;
import be.kuleuven.cs.distrinet.rejuse.property.PropertyMutex;
import be.kuleuven.cs.distrinet.rejuse.property.PropertySet;

public abstract class ElementWithModifiersImpl extends ElementImpl implements ElementWithModifiers {

  /*************
   * MODIFIERS *
   *************/
  private Multi<Modifier> _modifiers = new Multi<Modifier>(this);

  public abstract ElementWithModifiersImpl clone();
  
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

  /**
   * Add the given modifier to this element.
   * @param modifier The modifier to be added.
   */
 /*@
   @ public behavior
   @
   @ pre modifier != null;
   @
   @ post modifiers().contains(modifier);
   @*/
  public void addModifier(Modifier modifier) {
  	add(_modifiers,modifier);
  }
  
  /**
   * Add all modifiers in the given collection to this element.
   * 
   * @param modifiers The collection that contains the modifiers that must be added.
   */
 /*@
   @ public behavior
   @
   @ pre modifiers != null;
   @ pre ! modifiers.contains(null);
   @
   @ post modifiers().containsAll(modifiers);
   @*/
  public void addModifiers(List<Modifier> modifiers) {
  	if(modifiers == null) {
  		throw new ChameleonProgrammerException("List passed to addModifiers is null");
  	} else {
  		for(Modifier modifier: modifiers) {
  			addModifier(modifier);
  		}
  	}
  }

  /**
   * Remove the given modifier from this element.
   * 
   * @param modifier The modifier that must be removed.
   */
 /*@
   @ public behavior
   @
   @ pre modifier != null;
   @
   @ post ! modifiers().contains(modifier);
   @*/
  public void removeModifier(Modifier modifier) {
  	remove(_modifiers,modifier);
  }

  /**
   * Check whether this element contains the given modifier.
   * 
   * @param modifier The modifier that is searched.
   */
 /*@
   @ public behavior
   @
   @ pre modifier != null;
   @
   @ post \result == modifiers().contains(modifier);
   @*/
  public boolean hasModifier(Modifier modifier) {
    return _modifiers.getOtherEnds().contains(modifier);
  }

  public PropertySet<Element,ChameleonProperty> declaredProperties() {
		PropertySet<Element,ChameleonProperty> result = new PropertySet<Element,ChameleonProperty>();
    for(Modifier modifier:modifiers()) {
      result.addAll(modifier.impliedProperties().properties());
    }
    return result;
  }

	public List<Modifier> modifiers(Property property) throws ModelException {
		List<Modifier> result = new ArrayList<Modifier>();
		for(Modifier modifier: modifiers()) {
			if(modifier.impliesTrue(property)) {
				result.add(modifier);
			}
		}
		return result;
	}

	@Override
	public List<Modifier> modifiers(PropertyMutex mutex) throws ModelException {
		Property property = property(mutex);
		List<Modifier> result = new ArrayList<Modifier>();
		for (Modifier mod : modifiers()) {
			if (mod.impliesTrue(property)) {
				result.add(mod);
			}
		}
		return result;
	}

}
