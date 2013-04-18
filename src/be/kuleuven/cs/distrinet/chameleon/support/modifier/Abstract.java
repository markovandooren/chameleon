package be.kuleuven.cs.distrinet.chameleon.support.modifier;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.modifier.ModifierImpl;
import be.kuleuven.cs.distrinet.chameleon.core.property.ChameleonProperty;
import be.kuleuven.cs.distrinet.chameleon.oo.language.ObjectOrientedLanguage;
import be.kuleuven.cs.distrinet.rejuse.property.PropertySet;


/**
 * The abstract modifier specifies that a declaration is not completely defined yet. 
 * 
 * @author Marko van Dooren
 */
public class Abstract extends ModifierImpl {

  public Abstract() {
  }

	@Override
	public Abstract clone() {
		return new Abstract();
	}

 /*@
   @ behavior
   @
   @ post \result.contains(language().ABSTRACT);
   @ post \result.size() == 1;
   @*/
  public PropertySet<Element,ChameleonProperty> impliedProperties() {
    return createSet(language(ObjectOrientedLanguage.class).ABSTRACT);
  }
  
}
