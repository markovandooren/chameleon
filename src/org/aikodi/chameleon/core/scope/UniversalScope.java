package org.aikodi.chameleon.core.scope;

import org.aikodi.chameleon.core.element.Element;


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
	
	@Override
	public int hashCode() {
		return 6768534;
	}

}
