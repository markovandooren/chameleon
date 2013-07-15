package be.kuleuven.cs.distrinet.chameleon.core.lookup;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;

public interface SelectionResult {

	public Declaration finalDeclaration() throws LookupException;
}
