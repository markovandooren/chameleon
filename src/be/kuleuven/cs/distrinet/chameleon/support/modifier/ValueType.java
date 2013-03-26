package be.kuleuven.cs.distrinet.chameleon.support.modifier;

import be.kuleuven.cs.distrinet.rejuse.property.PropertySet;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.modifier.ModifierImpl;
import be.kuleuven.cs.distrinet.chameleon.core.property.ChameleonProperty;
import be.kuleuven.cs.distrinet.chameleon.oo.language.ObjectOrientedLanguage;

/**
 * 
 * @author Marko van Dooren
 */
public class ValueType extends ModifierImpl {
	
	public ValueType() {
		
	}

	@Override
	public ValueType clone() {
		return new ValueType();
	}

  public PropertySet<Element,ChameleonProperty> impliedProperties() {
    return createSet(language(ObjectOrientedLanguage.class).VALUE_TYPE);
  }

}

