package org.aikodi.chameleon.core.reference;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.event.name.NameChanged;
import org.aikodi.chameleon.core.lookup.DeclarationCollector;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;

/**
 * 
 * 
 * @author Marko van Dooren
 */
public abstract class ElementReference<D extends Declaration> extends CommonCrossReferenceWithTarget<D> implements CrossReferenceWithName<D>, CrossReferenceWithTarget<D> {

	
 /*@
   @ public behavior
   @
   @ pre name != null;
   @
   @ post getName().equals(name);
   @*/
	public ElementReference(String name) {
	   super(null);
		setName(name);
	}

  private String _name;

   @Override
   public String name() {
      return _name;
   }
  
	@Override
	public final void setName(String name) {
		if(name == null) {
			throw new ChameleonProgrammerException("The name of an element reference cannot be null");
		} else if(name.equals("")) {
			throw new ChameleonProgrammerException("The name of an element reference cannot be the empty string");
		}
		flushLocalCache();
		String old = _name;
		_name = name;
    if(changeNotificationEnabled()) {
      notify(new NameChanged(old, name));
    }
	}
  
  
	@Override
   public String toString() {
		return (getTarget() == null ? "" : getTarget().toString()+".")+name();
	}

}
