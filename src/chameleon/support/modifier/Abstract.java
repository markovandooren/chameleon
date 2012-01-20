package chameleon.support.modifier;

import org.rejuse.property.PropertySet;

import chameleon.core.element.Element;
import chameleon.core.modifier.ModifierImpl;
import chameleon.core.property.ChameleonProperty;
import chameleon.oo.language.ObjectOrientedLanguage;


/**
 * @author Marko van Dooren
 */
public class Abstract extends ModifierImpl<Abstract> {

  public Abstract() {
    
  }

	@Override
	public Abstract clone() {
		return new Abstract();
	}

 /*@
   @ behavior
   @
   @ post \result.contains(language().OVERRIDABLE);
   @ post \result.contains(language().DEFINED.inverse());
   @ post \result.size() == 2;
   @*/
  public PropertySet<Element,ChameleonProperty> impliedProperties() {
    return createSet(language(ObjectOrientedLanguage.class).OVERRIDABLE,language(ObjectOrientedLanguage.class).DEFINED.inverse());
  }
  
}
