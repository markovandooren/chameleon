package be.kuleuven.cs.distrinet.chameleon.support.statement;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.DeclarationContainer;
import be.kuleuven.cs.distrinet.chameleon.core.element.ElementImpl;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupStrategy;

public abstract class ForControl extends ElementImpl implements DeclarationContainer {
	
//	public abstract List<? extends Variable> declarations() throws LookupException;
	
	public abstract ForControl clone();

	@Override
	public LookupStrategy localStrategy() throws LookupException {
		return language().lookupFactory().createLocalLookupStrategy(this);
	}
	
	public <D extends Declaration> List<D> declarations(DeclarationSelector<D> selector) throws LookupException {
		return selector.selection(declarations());
	}

	public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
		return declarations();
	}


}
