package chameleon.core.declaration;

import java.util.List;

import chameleon.core.element.Element;
import chameleon.core.element.ElementImpl;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;

public abstract class DeclarationContainerImpl extends ElementImpl implements DeclarationContainer {

	@Override
	public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
		return declarations();
	}

	@Override
	public <D extends Declaration> List<D> declarations(DeclarationSelector<D> selector) throws LookupException {
		return selector.selection(declarations());
	}

	@Override
	public LookupStrategy localStrategy() throws LookupException {
		return language().lookupFactory().createLocalLookupStrategy(this);
	}

  @Override
  public LookupStrategy lexicalLookupStrategy(Element child) throws LookupException {
  	return language().lookupFactory().createLexicalLookupStrategy(localStrategy(), this);
  }
}
