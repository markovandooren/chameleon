package org.aikodi.chameleon.core.scope;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;

public class LexicalScope extends Scope {

 /*@
   @ public behavior
   @
   @ pre element != null;
   @
   @ post element() = element;
   @*/
	public LexicalScope(Element element) {
		setElement(element);
	}
	
	private void setElement(Element element) {
		_element = element;
	}
	
	private Element _element;

 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public Element element() {
		return _element;
	}

	@Override
	public boolean contains(Element element) throws LookupException {
		return element.equals(element()) || element.lexical().ancestors().contains(element());
	}

	@Override
	protected boolean geRecursive(Scope other) throws LookupException {
		return (other instanceof LexicalScope) && (contains(((LexicalScope)other).element()));
	}
}
