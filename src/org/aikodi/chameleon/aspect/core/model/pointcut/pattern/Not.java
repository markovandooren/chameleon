package org.aikodi.chameleon.aspect.core.model.pointcut.pattern;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.lookup.LookupException;

public class Not extends DeclarationPattern {

	/**
	 * @param declarationPattern
	 */
	public Not(DeclarationPattern nested) {
		this._nested = nested;
	}

	@Override
	public boolean eval(Declaration declaration) throws LookupException {
		return ! this._nested.eval(declaration);
	}
	
	public DeclarationPattern nested() {
		return _nested;
	}
	
	@Override
   public Not clone() {
		return new Not(nested().clone());
	}
	
	private final DeclarationPattern _nested;
}
