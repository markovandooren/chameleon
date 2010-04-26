package chameleon.core.lookup;

import java.util.List;

import org.rejuse.association.OrderedMultiAssociation;

import chameleon.core.element.Element;
import chameleon.core.namespace.NamespaceElementImpl;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.type.ClassBody;
import chameleon.oo.type.TypeElement;

public class LookupRedirector extends NamespaceElementImpl<LookupRedirector,Element> {

	public LookupRedirector() {
		
	}
	
	public LookupRedirector(Element contextElement) {
		setContextElement(contextElement);
	}
	
	@Override
	public LookupRedirector clone() {
		LookupRedirector result = new LookupRedirector(contextElement());
		for(Element element: elements()) {
			result.add(element.clone());
		}
		return result;
	}

	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}

	public List<? extends Element> children() {
		return elements();
	}
	
	public Element contextElement() {
		return _context;
	}
	
	public void setContextElement(Element element) {
		_context = element;
	}
	
	private Element _context;

	private OrderedMultiAssociation<LookupRedirector, Element> _elements = new OrderedMultiAssociation<LookupRedirector, Element>(this);

	public void add(Element element) {
	  if(element != null) {
	    _elements.add(element.parentLink());
	  }
	}
	
	public void remove(Element element) {
	  if(element != null) {
	    _elements.remove(element.parentLink());
	  }
	}
	
	public List<Element> elements() {
		return _elements.getOtherEnds();
	}

	@Override
	public LookupStrategy lexicalLookupStrategy(Element child) throws LookupException {
		return contextElement().lexicalLookupStrategy(child);
	}


}
