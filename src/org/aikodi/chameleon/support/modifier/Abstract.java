package org.aikodi.chameleon.support.modifier;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.modifier.ModifierImpl;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;

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
	public Abstract cloneSelf() {
		return new Abstract();
	}

 /*@
   @ behavior
   @
   @ post \result.contains(language().ABSTRACT);
   @ post \result.size() == 1;
   @*/
  @Override
public PropertySet<Element,ChameleonProperty> impliedProperties() {
    return createSet(language(ObjectOrientedLanguage.class).ABSTRACT);
  }
  
}
