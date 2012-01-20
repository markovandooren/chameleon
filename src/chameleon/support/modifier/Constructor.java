package chameleon.support.modifier;

import org.rejuse.property.PropertySet;

import chameleon.core.element.Element;
import chameleon.core.modifier.ModifierImpl;
import chameleon.core.property.ChameleonProperty;
import chameleon.oo.language.ObjectOrientedLanguage;

/**
 * A class of modifiers marking a method as being a constructor.
 * 
 * @author Marko van Dooren
 */
public class Constructor extends ModifierImpl<Constructor> {
	
	  public Constructor() {
		  
	  }

		@Override
		public Constructor clone() {
			return new Constructor();
		}

		/**
		 * A constructor modifier assigns the language().CONSTRUCTOR property to
		 * an element. Subclasses can add additional properties.
		 */
	 /*@
	   @ public behavior
	   @
	   @ post \result.contains(language().CONSTRUCTOR);
	   @*/
    public PropertySet<Element,ChameleonProperty> impliedProperties() {
      return createSet(language(ObjectOrientedLanguage.class).CONSTRUCTOR);
    }

}
