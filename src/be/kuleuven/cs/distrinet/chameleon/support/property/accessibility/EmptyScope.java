package be.kuleuven.cs.distrinet.chameleon.support.property.accessibility;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.scope.Scope;

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
