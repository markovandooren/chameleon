package chameleon.core.declaration;

import java.util.Collection;
import java.util.List;

import org.rejuse.association.OrderedMultiAssociation;

import chameleon.core.element.Element;
import chameleon.core.element.ElementImpl;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;

public class StubDeclarationContainer extends ElementImpl implements DeclarationContainer {

	public void add(Declaration declaration) {
		setAsParent(_declarations, declaration);
	}
	
	public void addAll(Collection<Declaration> declarations) {
		for(Declaration declaration: declarations) {
			add(declaration);
		}
	}
	
	@Override
	public List<? extends Element> children() {
		return declarations();
	}

	@Override
	public List<? extends Declaration> declarations() {
		return _declarations.getOtherEnds();
	}
	
	private OrderedMultiAssociation<StubDeclarationContainer, Declaration> _declarations = new OrderedMultiAssociation<StubDeclarationContainer, Declaration>(this); 

	@Override
	public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
		return _declarations.getOtherEnds();
	}

	@Override
	public <D extends Declaration> List<D> declarations(DeclarationSelector<D> selector) throws LookupException {
		return selector.selection(declarations());
	}

	@Override
	public StubDeclarationContainer clone() {
		StubDeclarationContainer result = new StubDeclarationContainer();
		for(Declaration declaration: declarations()) {
			result.add(declaration.clone());
		}
		return result;
	}

	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}

	@Override
	public LookupStrategy lexicalLookupStrategy(Element child) throws LookupException {
		if(_lexical == null) {
			_lexical = language().lookupFactory().createLexicalLookupStrategy(targetContext(), this);
		}
		return _lexical;
	}
	
	public LookupStrategy targetContext() throws LookupException {
		if(_local == null) {
			_local = language().lookupFactory().createLocalLookupStrategy(this);
		}
		return _local;
	}

	private LookupStrategy _local;
	private LookupStrategy _lexical;

	@Override
	public LookupStrategy localStrategy() throws LookupException {
		return targetContext();
	}
}
