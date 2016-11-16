package org.aikodi.chameleon.support.modifier;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.modifier.ModifierImpl;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.rejuse.property.PropertySet;


/**
 * @author Marko van Dooren
 */
public class Final extends ModifierImpl {

  public Final() {
  }

	@Override
	protected Final cloneSelf() {
		return new Final();
	}

 /*@
   @ behavior
   @
   @ post \result.contains(language().OVERRIDABLE.inverse());
   @ post \result.contains(language().DEFINED);
   @ post \result.size() == 2;
   @*/
  @Override
public PropertySet<Element,ChameleonProperty> impliedProperties() {
//    return createSet(language(ObjectOrientedLanguage.class).REFINABLE.inverse(),language(ObjectOrientedLanguage.class).DEFINED);
    ObjectOrientedLanguage language = language(ObjectOrientedLanguage.class);
		return createSet(language.FINAL);
  }

}
