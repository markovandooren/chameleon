package be.kuleuven.cs.distrinet.chameleon.support.modifier;

import be.kuleuven.cs.distrinet.rejuse.property.PropertySet;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.modifier.ModifierImpl;
import be.kuleuven.cs.distrinet.chameleon.core.property.ChameleonProperty;
import be.kuleuven.cs.distrinet.chameleon.oo.language.ObjectOrientedLanguage;

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
