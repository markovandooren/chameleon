package be.kuleuven.cs.distrinet.chameleon.core.declaration;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.element.ElementImpl;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.SelectionResult;

/**
 * A convenience class for implementation declaration containers.
 * 
 * @author Marko van Dooren
 */
public abstract class DeclarationContainerImpl extends ElementImpl implements DeclarationContainer {

	@Override
	public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
		return declarations();
	}

	@Override
	public <D extends Declaration> List<? extends SelectionResult> declarations(DeclarationSelector<D> selector) throws LookupException {
		return selector.selection(declarations());
	}

	@Override
	public LookupContext localContext() throws LookupException {
		return language().lookupFactory().createLocalLookupStrategy(this);
	}

  @Override
  public LookupContext lookupContext(Element child) throws LookupException {
  	return language().lookupFactory().createLexicalLookupStrategy(localContext(), this);
  }
}
