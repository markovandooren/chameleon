package chameleon.core.property;

import org.rejuse.logic.ternary.Ternary;
import org.rejuse.property.PropertyMutex;
import org.rejuse.property.PropertySet;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.Definition;
import chameleon.core.element.Element;
import chameleon.core.language.Language;
import chameleon.core.lookup.LookupException;

public class Defined extends DynamicChameleonProperty {

  public Defined(String name, Language lang) {
    super(name, lang, new PropertyMutex<ChameleonProperty>(), Declaration.class);
  }

  public Defined(String name, Language lang, PropertyMutex<ChameleonProperty> mutex) {
    super(name, lang, mutex,Declaration.class);
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
		PropertySet<Element,ChameleonProperty> declared = element.declaredProperties();
		Ternary result = declared.implies(this);
		if(result == Ternary.UNKNOWN) {
			if(element instanceof Declaration) {
				if(result == Ternary.UNKNOWN) {
					try {
						result = ((Declaration)element).complete() ? Ternary.TRUE : Ternary.FALSE;
					} catch (LookupException e) {
						result = Ternary.UNKNOWN; //not required, but allows breakpoint 
					}
				}
			} else {
				result = Ternary.FALSE;
			}
		}
    return result;
  }

}
