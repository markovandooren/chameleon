package be.kuleuven.cs.distrinet.chameleon.support.modifier;

import be.kuleuven.cs.distrinet.rejuse.property.PropertySet;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.modifier.ModifierImpl;
import be.kuleuven.cs.distrinet.chameleon.core.property.ChameleonProperty;
import be.kuleuven.cs.distrinet.chameleon.oo.language.ObjectOrientedLanguage;

public class Enum extends ModifierImpl {
	
	  public Enum() {
		  
	  }

		@Override
		public Enum clone() {
			return new Enum();
		}

    public PropertySet<Element,ChameleonProperty> impliedProperties() {
      return createSet(language(ObjectOrientedLanguage.class).EXTENSIBLE);
    }
}
