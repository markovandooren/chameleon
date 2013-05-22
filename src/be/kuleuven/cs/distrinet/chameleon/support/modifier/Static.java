package be.kuleuven.cs.distrinet.chameleon.support.modifier;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.modifier.ModifierImpl;
import be.kuleuven.cs.distrinet.chameleon.core.property.ChameleonProperty;
import be.kuleuven.cs.distrinet.chameleon.oo.language.ObjectOrientedLanguage;
import be.kuleuven.cs.distrinet.rejuse.property.PropertySet;

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
 public PropertySet<Element,ChameleonProperty> impliedProperties() {
   return createSet(language(ObjectOrientedLanguage.class).CLASS);
 }

}
