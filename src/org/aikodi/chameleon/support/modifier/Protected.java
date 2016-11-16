package org.aikodi.chameleon.support.modifier;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.modifier.ModifierImpl;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.rejuse.property.PropertySet;

/**
 * @author Marko van Dooren
 */
public class Protected extends ModifierImpl {

	
	
  public Protected() {
    
  }
  
	@Override
	protected Protected cloneSelf() {
		return new Protected();
	}

	@Override
   public PropertySet<Element,ChameleonProperty> impliedProperties() {
		return createSet(language().property(ProtectedProperty.ID));
	}


}
