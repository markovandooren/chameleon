package chameleon.oo.type.generics;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.lookup.LookupException;
import chameleon.oo.type.Type;

public class CapturedTypeParameter extends FormalTypeParameter {

	public CapturedTypeParameter(SimpleNameSignature signature) {
		super(signature);
	}

	@Override
	public CapturedTypeParameter clone() {
		CapturedTypeParameter result = new CapturedTypeParameter(signature().clone());
		for(TypeConstraint constraint: constraints()) {
			result.addConstraint(constraint.clone());
		}
		return result;
	}

	@Override
	protected Type createLazyAlias() {
		return new AbstractInstantiatedTypeParameter.LazyTypeAlias(signature().clone(), this);
	}

	@Override
	protected Type createSelectionType() throws LookupException {
		return new ActualType(signature().clone(), upperBound(),this);
	}

}