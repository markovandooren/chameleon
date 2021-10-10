package org.aikodi.chameleon.core.property;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.rejuse.logic.ternary.Ternary;
import org.aikodi.rejuse.property.PropertyMutex;

public class Defined extends DynamicChameleonProperty {

  public Defined(String name) {
    super(name, new PropertyMutex<ChameleonProperty>(), Declaration.class);
  }

  public Defined(String name, PropertyMutex<ChameleonProperty> mutex) {
    super(name, mutex,Declaration.class);
  }
  
  @Override
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
