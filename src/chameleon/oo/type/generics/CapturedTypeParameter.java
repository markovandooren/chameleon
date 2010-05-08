package chameleon.oo.type.generics;

import java.util.ArrayList;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.oo.type.Type;

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

	@Override
	protected Type createLazyAlias() {
		return new AbstractInstantiatedTypeParameter.LazyTypeAlias(signature().clone(), this);
	}

	@Override
	protected Type createSelectionType() throws LookupException {
		return new AbstractInstantiatedTypeParameter.ActualType(signature().clone(), upperBound());
	}

	
//	@Override
//	public boolean uniSameAs(Element other) throws LookupException {
//		return other instanceof CapturedTypeParameter && compatibleWith((TypeParameter) other, new ArrayList());
//	}
	
	

	
//	public boolean compatibleWith(TypeParameter other) throws LookupException {
//		throw new Error();
//	}

}
