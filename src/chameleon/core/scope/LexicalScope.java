package chameleon.core.scope;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespace.NamespaceElement;

public class LexicalScope extends Scope {

 /*@
   @ public behavior
   @
   @ pre element != null;
   @
   @ post element() = element;
   @*/
	public LexicalScope(NamespaceElement element) {
		setElement(element);
	}
	
	private void setElement(NamespaceElement element) {
		_element = element;
	}
	
	private NamespaceElement _element;

 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public NamespaceElement<?,?> element() {
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
