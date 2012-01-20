package chameleon.support.property.accessibility;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.scope.Scope;

/**
 * @author Marko van Dooren
 */
public class EmptyScope extends Scope {

 /*@
   @ public behavior
   @
   @ \result == false;
   @*/
	public boolean contains(Element element) {
		return false;
	}
	
 /*@
   @ public behavior
   @
   @ post \result == other instanceof EmptyScope;
   @*/
  public boolean geRecursive(Scope other) throws LookupException {
    return (other instanceof EmptyScope);
  }
  
 /*@
   @ public behavior
   @
   @ post \result == true; 
   @*/
  public boolean leRecursive(Scope other) {
    return true;
  }
  
  public boolean equals(Object o) {
    return (o instanceof EmptyScope);
  }

}
