package chameleon.core.property;

import org.rejuse.logic.ternary.Ternary;
import org.rejuse.property.DynamicProperty;
import org.rejuse.property.PropertyImpl;
import org.rejuse.property.PropertyMutex;
import org.rejuse.property.PropertySet;
import org.rejuse.property.PropertyUniverse;

import chameleon.core.declaration.Definition;
import chameleon.core.element.Element;
import chameleon.core.language.Language;
import chameleon.core.validation.VerificationResult;

public class Defined extends DynamicProperty<Element,ChameleonProperty> implements ChameleonProperty {

  public Defined(String name, Language lang) {
    super(name, lang, new PropertyMutex<ChameleonProperty>());
  }

  public Defined(String name, Language lang, PropertyMutex<ChameleonProperty> mutex) {
    super(name, lang, mutex);
  }
  
  protected Defined(String name, Language lang, PropertyMutex<ChameleonProperty> mutex, ChameleonProperty inverse) {
    super(name, lang, mutex, inverse);
  }

  /**
   * An object is defined if and only if it is a Definition, and
   * it is complete. 
   */
 /*@
   @ behavior
   @
   @ post \result == (element instanceof Definition) && ((Definition)element).complete();
   @*/
  @Override public Ternary appliesTo(Element element) {
  	Ternary result;
  	if((element instanceof Definition)) {
      if(((Definition)element).complete()) {
      	// Definitely defined
    	  result = Ternary.TRUE;
      } else {
      	// Unless explicitly declared as defined, it is undefined.
      	PropertySet<Element,ChameleonProperty> declared =element.declaredProperties();
      	result = declared.implies(this);
      	if(result == Ternary.UNKNOWN) {
          result = Ternary.FALSE;
      	}
      }
  	} else {
  		// Can't say
  		result = Ternary.UNKNOWN;
  	}
    return result;
  }

	@Override
	protected void createInverse(String name, PropertyUniverse<ChameleonProperty> universe) {
		name;
	}

	public VerificationResult verify(Element element) {
	}

}
