package org.aikodi.chameleon.support.modifier;



import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.modifier.ModifierImpl;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;

import be.kuleuven.cs.distrinet.rejuse.property.PropertySet;

/**
 * @author Marko van Dooren
 */
public class Native extends ModifierImpl {

  public Native() {
    
  }

	@Override
	protected Native cloneSelf() {
		return new Native();
	}
	
 /*@
   @ behavior
   @
   @ post \result.contains(language(ObjectOrientedLanguage.class).OVERRIDABLE);
   @ post \result.contains(language(ObjectOrientedLanguage.class).DEFINED);
   @ post \result.size() == 2;
   @*/
  @Override
public PropertySet<Element,ChameleonProperty> impliedProperties() {
    return createSet(language(ObjectOrientedLanguage.class).NATIVE);
  }

}
