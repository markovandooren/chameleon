package be.kuleuven.cs.distrinet.chameleon.support.modifier;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.modifier.ModifierImpl;
import be.kuleuven.cs.distrinet.chameleon.core.property.ChameleonProperty;
import be.kuleuven.cs.distrinet.chameleon.oo.language.ObjectOrientedLanguage;
import be.kuleuven.cs.distrinet.rejuse.property.PropertySet;

/**
 * @author Tim Laeremans
 * @author Marko van Dooren
 */
public class Destructor extends ModifierImpl {

	public Destructor() {
		
	}

	@Override
	protected Destructor cloneSelf() {
		return new Destructor();
	}

  public PropertySet<Element,ChameleonProperty> impliedProperties() {
    return createSet(language(ObjectOrientedLanguage.class).DESTRUCTOR);
  }
}
