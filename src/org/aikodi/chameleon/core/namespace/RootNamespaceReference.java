package org.aikodi.chameleon.core.namespace;

import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.chameleon.workspace.View;

public class RootNamespaceReference extends ElementImpl implements CrossReference<Namespace> {

	@Override
	public Namespace getElement() throws LookupException {
		View view = view();
		if(view == null) {
		  throw new LookupException("This root namespace reference is not part of a view.");
		}
		return view.namespace();
	}

	/**
	* @{inheritDoc}
	*/
	@Override
	public Class<Namespace> referencedType() {
	  return Namespace.class;
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

