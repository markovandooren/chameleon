package org.aikodi.chameleon.core.namespace;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.reference.CrossReference;

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
	
	@Override
   public String toString() {
		return "";
	}
	
}

