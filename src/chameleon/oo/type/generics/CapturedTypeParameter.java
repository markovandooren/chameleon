package chameleon.oo.type.generics;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.lookup.LookupException;

public class CapturedTypeParameter extends FormalTypeParameter {

	public CapturedTypeParameter(SimpleNameSignature signature) {
		super(signature);
	}

	@Override
	public FormalTypeParameter clone() {
		FormalTypeParameter result = new FormalTypeParameter(signature().clone());
		for(TypeConstraint constraint: constraints()) {
			result.addConstraint(constraint.clone());
		}
		return result;
	}

	
//	public boolean compatibleWith(TypeParameter other) throws LookupException {
//		throw new Error();
//	}

}
