package be.kuleuven.cs.distrinet.chameleon.core.scope;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;


/**
 * @author Marko van Dooren
 */
public class UniversalScope extends Scope {

 /*@
   @ public behavior
   @
   @ post \result == true;
   @*/
	public boolean contains(Element element) {
		return true;
	}
	
 /*@
   @ also public behavior
   @
   @ post \result == true;
   @*/
  public boolean geRecursive(Scope other) {
    return true;
  }
  
 /*@
   @ public behavior
   @
   @ post \result == other instanceof UniversalScope;
   @*/
  public boolean leRecursive(Scope other) {
  	return other instanceof UniversalScope;
  }

 /*@
   @ public behavior
   @
   @ post \result == other instanceof UniversalScope;
   @*/
  public boolean equals(Object o) {
    return (o instanceof UniversalScope);
  }

}
