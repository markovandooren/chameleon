package be.kuleuven.cs.distrinet.chameleon.support.modifier;

import be.kuleuven.cs.distrinet.rejuse.property.PropertySet;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.modifier.ModifierImpl;
import be.kuleuven.cs.distrinet.chameleon.core.property.ChameleonProperty;
import be.kuleuven.cs.distrinet.chameleon.oo.language.ObjectOrientedLanguage;

/**
 * @author Marko van Dooren
 */
public class Private extends ModifierImpl {

  public Private() {
    
  }
  
	@Override
	public Private clone() {
		return new Private();
	}

	/**
	 * A private element has a private scope, and is not inheritable.
	 */
  public PropertySet<Element,ChameleonProperty> impliedProperties() {
	  return createSet(language().property(PrivateProperty.ID), language(ObjectOrientedLanguage.class).INHERITABLE.inverse());
  }

}
