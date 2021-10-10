package org.aikodi.chameleon.support.modifier;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.modifier.ModifierImpl;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.rejuse.property.PropertySet;

/**
 * @author Marko van Dooren
 */
public class Static extends ModifierImpl {

	public Static() {
		
	}

	@Override
	protected Static cloneSelf() {
		return new Static();
	}
	 /*@
  @ behavior
  @
  @ post \result.contains(language(ObjectOrientedLanguage.class).OVERRIDABLE.inverse());
  @ post \result.contains(language(ObjectOrientedLanguage.class).DEFINED);
  @ post \result.size() == 2;
  @*/
 @Override
public PropertySet<Element,ChameleonProperty> impliedProperties() {
   return createSet(language(ObjectOrientedLanguage.class).CLASS());
 }

}
