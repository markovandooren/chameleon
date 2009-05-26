package chameleon.core.scope;

import chameleon.core.element.Element;


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

  public boolean equals(Object o) {
    return (o instanceof UniversalScope);
  }

}
