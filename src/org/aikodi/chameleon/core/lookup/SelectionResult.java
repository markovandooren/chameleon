package org.aikodi.chameleon.core.lookup;

import org.aikodi.chameleon.core.declaration.Declaration;

/**
 * An object that can be the result of a lookup. This does not have to
 * be a model element.
 * 
 * @author Marko van Dooren
 */
public interface SelectionResult {

	public Declaration finalDeclaration() throws LookupException;
	
	public SelectionResult updatedTo(Declaration declaration);
	
	public Declaration template();
}
