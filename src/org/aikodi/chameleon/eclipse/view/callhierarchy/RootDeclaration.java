package org.aikodi.chameleon.eclipse.view.callhierarchy;

import org.aikodi.chameleon.core.declaration.Declaration;

/**
 * Used to encapsulate the root method of the call hierarchy.
 * Otherwise the root method is not visible.
 * 
 * 
 * @author Tim Vermeiren
 * @author Marko van Dooren
 */
public class RootDeclaration {
	
	private Declaration _declaration;

	/**
	 * @param declaration
	 */
	public RootDeclaration(Declaration declaration) {
		this._declaration = declaration;
	}
	
	public Declaration getDeclaration(){
		return _declaration;
	}
	
}
