package org.aikodi.chameleon.stub;

import java.util.Collection;
import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.DeclarationContainer;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.SelectionResult;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.util.association.Multi;

/**
 * A stub declaration that can be used e.g. for testing.
 * 
 * @author Marko van Dooren
 */
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
	public <D extends Declaration> List<? extends SelectionResult<D>> declarations(DeclarationSelector<D> selector) throws LookupException {
		return selector.selection(declarations());
	}

	@Override
	public StubDeclarationContainer cloneSelf() {
		return new StubDeclarationContainer();
	}

	@Override
	public Verification verifySelf() {
		return Valid.create();
	}

	@Override
	public LookupContext lookupContext(Element child) throws LookupException {
		if(_lexical == null) {
			_lexical = language().lookupFactory().createLexicalLookupStrategy(targetContext(), this);
		}
		return _lexical;
	}
	
	public LookupContext targetContext() throws LookupException {
		if(_local == null) {
			_local = language().lookupFactory().createLocalLookupStrategy(this);
		}
		return _local;
	}

	private LookupContext _local;
	private LookupContext _lexical;

	@Override
	public LookupContext localContext() throws LookupException {
		return targetContext();
	}
}
