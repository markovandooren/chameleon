package org.aikodi.chameleon.aspect.core.model.pointcut.pattern;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.lookup.LookupException;

public class And extends Dual {
	
	public And(DeclarationPattern first, DeclarationPattern second) {
		super(first,second);
	}

	@Override
	public boolean eval(Declaration declaration) throws LookupException {
		return first().eval(declaration) && second().eval(declaration);
	}

	@Override
	public DeclarationPattern clone() {
		return new And(first().clone(),second().clone());
	}
}
