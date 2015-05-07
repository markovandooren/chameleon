package org.aikodi.chameleon.oo.type;

import org.aikodi.chameleon.core.declaration.BasicDeclaration;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.SimpleNameSignature;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.SelectionResult;

/**
 * A class of parameters.
 * 
 * @author Marko van Dooren
 */
public abstract class Parameter extends BasicDeclaration {

	/**
	 * Create a new parameter with the given name.
	 * 
	 * @param name The name of the parameter. The name cannot be null.
	 */
	public Parameter(String name) {
		super(name);
	}
	
	protected Parameter() {
	  
	}
	
	@Override
  public abstract Declaration selectionDeclaration() throws LookupException;
	
	public Class<SimpleNameSignature> signatureType() {
		return SimpleNameSignature.class;
	}
	
	@Override
	public SelectionResult updatedTo(Declaration declaration) {
		return declaration;
	}

}
