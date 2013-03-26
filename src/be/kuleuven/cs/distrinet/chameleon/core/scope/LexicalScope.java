package be.kuleuven.cs.distrinet.chameleon.core.scope;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;

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
		return element.equals(element()) || element.ancestors().contains(element());
	}

	@Override
	protected boolean geRecursive(Scope other) throws LookupException {
		return (other instanceof LexicalScope) && (contains(((LexicalScope)other).element()));
	}
}
