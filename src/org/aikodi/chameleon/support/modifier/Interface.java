package org.aikodi.chameleon.support.modifier;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.modifier.ModifierImpl;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.rejuse.property.PropertySet;

public class Interface extends ModifierImpl {
	
	  public Interface() {
		  
	  }

		@Override
		protected Interface cloneSelf() {
			return new Interface();
		}

		/**
		 * An interface is abstract, thus not defined.
		 */
    @Override
   public PropertySet<Element,ChameleonProperty> impliedProperties() {
      return createSet(language(ObjectOrientedLanguage.class).INTERFACE);
    }
}
