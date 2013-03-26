package be.kuleuven.cs.distrinet.chameleon.core.declaration;

import java.util.Collection;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.element.ElementImpl;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupStrategy;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.VerificationResult;
import be.kuleuven.cs.distrinet.chameleon.util.association.Multi;

public class StubDeclarationContainer extends ElementImpl implements DeclarationContainer {

	public void add(Declaration declaration) {
		add(_declarations, declaration);
	}
	
	public void addAll(Collection<Declaration> declarations) {
		for(Declaration declaration: declarations) {
			add(declaration);
		}
	}
	
	@Override
	public List<? extends Declaration> declarations() {
		return _declarations.getOtherEnds();
	}
	
	private Multi<Declaration> _declarations = new Multi<Declaration>(this); 

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
