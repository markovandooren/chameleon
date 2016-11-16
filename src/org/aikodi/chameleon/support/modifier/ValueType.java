package org.aikodi.chameleon.support.modifier;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.modifier.ModifierImpl;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.rejuse.property.PropertySet;

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

  @Override
public PropertySet<Element,ChameleonProperty> impliedProperties() {
    return createSet(language(ObjectOrientedLanguage.class).VALUE_TYPE);
  }

}

