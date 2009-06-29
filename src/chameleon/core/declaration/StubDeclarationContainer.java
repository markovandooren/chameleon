package chameleon.core.declaration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rejuse.association.OrderedReferenceSet;

import chameleon.core.context.Context;
import chameleon.core.context.ContextFactory;
import chameleon.core.context.LookupException;
import chameleon.core.element.Element;
import chameleon.core.element.ElementImpl;

public class StubDeclarationContainer extends ElementImpl<StubDeclarationContainer,DeclarationContainer> implements DeclarationContainer<StubDeclarationContainer,DeclarationContainer>{

	@Override
	public StubDeclarationContainer clone() {
		StubDeclarationContainer result = new StubDeclarationContainer();
		for(Declaration el: children()) {
			result.add(el.clone());
		}
		return result;
	}

	public Set<Declaration> declarations() throws LookupException {
		return new HashSet<Declaration>(_elements.getOtherEnds());
	}

	public List<? extends Declaration> children() {
    return _elements.getOtherEnds();
	}

	private OrderedReferenceSet<StubDeclarationContainer, Declaration> _elements = new OrderedReferenceSet<StubDeclarationContainer, Declaration>(this);

	public void add(Declaration element) {
	  if(element != null) {
	    _elements.add(element.parentLink());
	  }
	}

  public Context lexicalContext(Element element) {
  	ContextFactory factory = language().contextFactory();
  	return factory.createLexicalContext(this,factory.createTargetContext(this));
  }

}
