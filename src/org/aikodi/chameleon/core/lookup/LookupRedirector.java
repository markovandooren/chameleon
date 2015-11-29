package org.aikodi.chameleon.core.lookup;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.util.association.Single;

/**
 * A class of elements that is used to redirect the lookup
 * for generated elements.
 * 
 * @author Marko van Dooren
 */
public class LookupRedirector extends ElementImpl implements Stub {

	public LookupRedirector(Element contextElement) {
		setContextElement(contextElement);
	}
	
	public LookupRedirector(Element contextElement, Element child) {
		setContextElement(contextElement);
		setChild(child);
	}
	
	@Override
	protected LookupRedirector cloneSelf() {
		LookupRedirector result = new LookupRedirector(contextElement(), null);
		return result;
	}

	@Override
	public Verification verifySelf() {
		return Valid.create();
	}

	public Element contextElement() {
		return _context;
	}
	
	public void setContextElement(Element element) {
		_context = element;
	}
	
	private Element _context;

	private Single<Element> _element = new Single<>(this);

	public void setChild(Element element) {
		set(_element, element);
	}
	
	@Override
   public Element child() {
		return _element.getOtherEnd();
	}

	@Override
	public LookupContext lookupContext(Element child) throws LookupException {
		return contextElement().lookupContext(child);
	}

	@Override
   public Element generator() {
		return contextElement();
	}

}
