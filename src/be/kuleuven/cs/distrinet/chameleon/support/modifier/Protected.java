package be.kuleuven.cs.distrinet.chameleon.support.modifier;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.modifier.ModifierImpl;
import be.kuleuven.cs.distrinet.chameleon.core.property.ChameleonProperty;
import be.kuleuven.cs.distrinet.rejuse.property.PropertySet;

/**
 * @author Marko van Dooren
 */
public class Protected extends ModifierImpl {

	
	
  public Protected() {
    
  }
  
	@Override
	public Protected clone() {
		return new Protected();
	}

	public PropertySet<Element,ChameleonProperty> impliedProperties() {
		return createSet(language().property(ProtectedProperty.ID));
	}


}
