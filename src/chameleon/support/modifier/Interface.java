package chameleon.support.modifier;

import org.rejuse.property.PropertySet;

import chameleon.core.element.Element;
import chameleon.core.modifier.ModifierImpl;
import chameleon.core.property.ChameleonProperty;
import chameleon.oo.language.ObjectOrientedLanguage;

public class Interface extends ModifierImpl {
	
	  public Interface() {
		  
	  }

		@Override
		public Interface clone() {
			return new Interface();
		}

		/**
		 * An interface is abstract, thus not defined.
		 */
    public PropertySet<Element,ChameleonProperty> impliedProperties() {
      return createSet(language(ObjectOrientedLanguage.class).INTERFACE);
    }
}
