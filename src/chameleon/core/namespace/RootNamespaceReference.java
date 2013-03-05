package chameleon.core.namespace;

import chameleon.core.declaration.Declaration;
import chameleon.core.element.ElementImpl;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.reference.CrossReference;

public class RootNamespaceReference extends ElementImpl implements CrossReference<Namespace> {

	@Override
	public LookupStrategy targetContext() throws LookupException {
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
	public RootNamespaceReference clone() {
		return new RootNamespaceReference();
	}
	
	public String toString() {
		return "";
	}
	
}

