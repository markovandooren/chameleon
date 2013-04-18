package be.kuleuven.cs.distrinet.chameleon.core.property;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.language.Language;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.rejuse.logic.ternary.Ternary;
import be.kuleuven.cs.distrinet.rejuse.property.PropertyMutex;

public class Defined extends DynamicChameleonProperty {

  public Defined(String name, Language lang) {
    super(name, lang, new PropertyMutex<ChameleonProperty>(), Declaration.class);
  }

  public Defined(String name, Language lang, PropertyMutex<ChameleonProperty> mutex) {
    super(name, lang, mutex,Declaration.class);
  }
  
  protected Ternary selfAppliesTo(Element element) {
  	Ternary result;
		if(element instanceof Declaration) {
			try {
				result = ((Declaration)element).complete() ? Ternary.TRUE : Ternary.FALSE;
			} catch (LookupException e) {
				result = Ternary.UNKNOWN; //not required, but allows breakpoint 
			}
		} else {
			result = Ternary.FALSE;
		}
		return result;
  }

}
