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
	@Override
   public boolean contains(Element element) {
		return true;
	}
	
 /*@
   @ also public behavior
   @
   @ post \result == true;
   @*/
  @Override
public boolean geRecursive(Scope other) {
    return true;
  }
  
 /*@
   @ public behavior
   @
   @ post \result == other instanceof UniversalScope;
   @*/
  @Override
public boolean leRecursive(Scope other) {
  	return other instanceof UniversalScope;
  }

 /*@
   @ public behavior
   @
   @ post \result == other instanceof UniversalScope;
   @*/
  @Override
public boolean equals(Object o) {
    return (o instanceof UniversalScope);
  }

}
