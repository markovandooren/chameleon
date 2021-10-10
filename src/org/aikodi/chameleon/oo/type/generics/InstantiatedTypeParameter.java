package org.aikodi.chameleon.oo.type.generics;

import org.aikodi.chameleon.core.lookup.LookupException;

public class InstantiatedTypeParameter extends AbstractInstantiatedTypeParameter {
	
	public InstantiatedTypeParameter(String name, TypeArgument argument) {
		super(name,argument);
	}
	
	@Override
	protected InstantiatedTypeParameter cloneSelf() {
		return new InstantiatedTypeParameter(name(),argument());
	}

	/**
	 * @return
	 * @throws LookupException 
	 */
	public boolean hasWildCardBound() throws LookupException {
		return argument().isWildCardBound();
	}

	
}
