package be.kuleuven.cs.distrinet.chameleon.core.namespace;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.element.ElementImpl;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.reference.CrossReference;

public class RootNamespaceReference extends ElementImpl implements CrossReference<Namespace> {

	@Override
	public LookupContext targetContext() throws LookupException {
		return getElement().targetContext();
	}

	@Override
	public Namespace getElement() throws LookupException {
		return view().namespace();
	}

	@Override
	public Declaration getDeclarator() throws LookupException {
		return getElement();
	}

	@Override
	protected RootNamespaceReference cloneSelf() {
		return new RootNamespaceReference();
	}
	
	public String toString() {
		return "";
	}
	
}

