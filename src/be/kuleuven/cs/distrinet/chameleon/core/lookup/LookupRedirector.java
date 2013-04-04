package be.kuleuven.cs.distrinet.chameleon.core.lookup;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.element.ElementImpl;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.VerificationResult;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

public class LookupRedirector extends ElementImpl implements Stub {

	public LookupRedirector(Element contextElement) {
		setContextElement(contextElement);
	}
	
	public LookupRedirector(Element contextElement, Declaration child) {
		setContextElement(contextElement);
		setChild(child);
	}
	
	@Override
	public LookupRedirector clone() {
		LookupRedirector result = new LookupRedirector(contextElement(), child().clone());
		return result;
	}

	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}

	public Element contextElement() {
		return _context;
	}
	
	public void setContextElement(Element element) {
		_context = element;
	}
	
	private Element _context;

	private Single<Declaration> _element = new Single<Declaration>(this);

	public void setChild(Declaration element) {
		set(_element, element);
	}
	
	public Declaration child() {
		return _element.getOtherEnd();
	}

	@Override
	public LookupContext lookupContext(Element child) throws LookupException {
		return contextElement().lookupContext(child);
	}

	public Element generator() {
		return contextElement();
	}

}
