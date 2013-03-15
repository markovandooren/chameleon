package chameleon.support.modifier;

import be.kuleuven.cs.distrinet.rejuse.property.PropertySet;
import chameleon.core.element.Element;
import chameleon.core.modifier.ModifierImpl;
import chameleon.core.property.ChameleonProperty;
import chameleon.oo.language.ObjectOrientedLanguage;

/**
 * @author Marko van Dooren
 */
public class Static extends ModifierImpl {

	public Static() {
		
	}

	@Override
	public Static clone() {
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
