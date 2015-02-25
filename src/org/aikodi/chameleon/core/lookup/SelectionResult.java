package org.aikodi.chameleon.core.lookup;

import org.aikodi.chameleon.core.declaration.Declaration;

public interface SelectionResult {

	public Declaration finalDeclaration() throws LookupException;
	
	public SelectionResult updatedTo(Declaration declaration);
	
	public Declaration template();
}
