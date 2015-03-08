package org.aikodi.chameleon.support.property.accessibility;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.scope.Scope;

/**
 * @author Marko van Dooren
 */
public class EmptyScope extends Scope {

 /*@
   @ public behavior
   @
   @ \result == false;
   @*/
	@Override
   public boolean contains(Element element) {
		return false;
	}
	
 /*@
   @ public behavior
   @
   @ post \result == other instanceof EmptyScope;
   @*/
  @Override
public boolean geRecursive(Scope other) throws LookupException {
    return (other instanceof EmptyScope);
  }
  
 /*@
   @ public behavior
   @
   @ post \result == true; 
   @*/
  @Override
public boolean leRecursive(Scope other) {
    return true;
  }
  
  @Override
public boolean equals(Object o) {
    return (o instanceof EmptyScope);
  }

}
