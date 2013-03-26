package be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.pattern;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;

public class Or extends Dual {

	Or(DeclarationPattern first, DeclarationPattern second) {
		super(first, second);
	}

	@Override
	public boolean eval(Declaration declaration) throws LookupException {
		return first().eval(declaration) || second().eval(declaration);
	}

	@Override
	public DeclarationPattern clone() {
		return new Or(first().clone(),second().clone());
	}
}
