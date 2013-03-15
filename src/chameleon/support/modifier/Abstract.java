package chameleon.support.modifier;

import be.kuleuven.cs.distrinet.rejuse.property.PropertySet;
import chameleon.core.element.Element;
import chameleon.core.modifier.ModifierImpl;
import chameleon.core.property.ChameleonProperty;
import chameleon.oo.language.ObjectOrientedLanguage;


/**
 * The abstract modifier specifies that a declaration is not completely defined yet. 
 * 
 * @author Marko van Dooren
 */
public class Abstract extends ModifierImpl {

  public Abstract() {
  }

	@Override
	public Abstract clone() {
		return new Abstract();
	}

 /*@
   @ behavior
   @
   @ post \result.contains(language().ABSTRACT);
   @ post \result.size() == 1;
   @*/
  public PropertySet<Element,ChameleonProperty> impliedProperties() {
    return createSet(language(ObjectOrientedLanguage.class).ABSTRACT);
  }
  
}
