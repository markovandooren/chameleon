package org.aikodi.chameleon.support.modifier;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.modifier.ModifierImpl;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.rejuse.property.PropertySet;

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

  @Override
public PropertySet<Element,ChameleonProperty> impliedProperties() {
    return createSet(language(ObjectOrientedLanguage.class).DESTRUCTOR);
  }
}
