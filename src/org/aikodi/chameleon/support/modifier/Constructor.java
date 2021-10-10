package org.aikodi.chameleon.support.modifier;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.modifier.ModifierImpl;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.rejuse.property.PropertySet;

/**
 * A class of modifiers marking a method as being a constructor.
 * 
 * @author Marko van Dooren
 */
public class Constructor extends ModifierImpl {
	
	  public Constructor() {
		  
	  }

		@Override
		protected Constructor cloneSelf() {
			return new Constructor();
		}

		/**
		 * A constructor modifier assigns the language().CONSTRUCTOR property to
		 * an element. Subclasses can add additional properties.
		 */
	 /*@
	   @ public behavior
	   @
	   @ post \result.contains(language(ObjectOrientedLanguage.class).CONSTRUCTOR());
	   @*/
    @Override
   public PropertySet<Element,ChameleonProperty> impliedProperties() {
      return createSet(language(ObjectOrientedLanguage.class).CONSTRUCTOR());
    }

}
