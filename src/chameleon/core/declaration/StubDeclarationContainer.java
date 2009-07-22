package chameleon.core.declaration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rejuse.association.OrderedReferenceSet;

import chameleon.core.element.Element;
import chameleon.core.element.ElementImpl;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.lookup.LookupStrategyFactory;

public class StubDeclarationContainer<T extends Declaration> extends ElementImpl<StubDeclarationContainer,DeclarationContainer> implements DeclarationContainer<StubDeclarationContainer,DeclarationContainer>{

	@Override
	public StubDeclarationContainer clone() {
		StubDeclarationContainer result = new StubDeclarationContainer();
		for(Declaration el: children()) {
			result.add(el.clone());
		}
		return result;
	}

	public List<T> declarations() throws LookupException {
		return _elements.getOtherEnds();
	}
	
	public <D extends Declaration> List<D> declarations(DeclarationSelector<D> selector) throws LookupException {
		return selector.selection(declarations());
	}

	public List<? extends Declaration> children() {
    return _elements.getOtherEnds();
	}

	private OrderedReferenceSet<StubDeclarationContainer, T> _elements = new OrderedReferenceSet<StubDeclarationContainer, T>(this);

	public void add(T element) {
	  if(element != null) {
	    _elements.add(element.parentLink());
	  }
	}

  public LookupStrategy lexicalContext(Element element) {
  	LookupStrategyFactory factory = language().lookupFactory();
  	return factory.createLexicalLookupStrategy(factory.createTargetLookupStrategy(this),this);
  }

}
