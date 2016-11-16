package org.aikodi.chameleon.support.modifier;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.modifier.ModifierImpl;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.rejuse.property.PropertySet;

/**
 * @author Marko van Dooren
 */
public class Private extends ModifierImpl {

  public Private() {
    
  }
  
	@Override
	protected Private cloneSelf() {
		return new Private();
	}

	/**
	 * A private element has a private scope, and is not inheritable.
	 */
  @Override
public PropertySet<Element,ChameleonProperty> impliedProperties() {
	  return createSet(language(ObjectOrientedLanguage.class).property(PrivateProperty.ID), language(ObjectOrientedLanguage.class).INHERITABLE.inverse());
  }

}
