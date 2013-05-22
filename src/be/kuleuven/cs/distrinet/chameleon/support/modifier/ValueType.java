package be.kuleuven.cs.distrinet.chameleon.support.modifier;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.modifier.ModifierImpl;
import be.kuleuven.cs.distrinet.chameleon.core.property.ChameleonProperty;
import be.kuleuven.cs.distrinet.chameleon.oo.language.ObjectOrientedLanguage;
import be.kuleuven.cs.distrinet.rejuse.property.PropertySet;

/**
 * 
 * @author Marko van Dooren
 */
public class ValueType extends ModifierImpl {
	
	public ValueType() {
		
	}

	@Override
	protected ValueType cloneSelf() {
		return new ValueType();
	}

  public PropertySet<Element,ChameleonProperty> impliedProperties() {
    return createSet(language(ObjectOrientedLanguage.class).VALUE_TYPE);
  }

}

