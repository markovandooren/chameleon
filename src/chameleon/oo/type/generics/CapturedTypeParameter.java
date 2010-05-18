package chameleon.oo.type.generics;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.oo.type.DerivedType;
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

	//FIXME this is dirty.
	@Override
	public boolean uniSameAs(Element other) throws LookupException {
		if(origin() == this) {
			if(other == other.origin()) {
			  // The real test is here.
				if(other instanceof CapturedTypeParameter) {
					boolean result = signature().sameAs(((CapturedTypeParameter) other).signature());
					if(result) {
					  result = nearestAncestor(DerivedType.class).baseType().sameAs(((DerivedType)other.nearestAncestor(DerivedType.class)).baseType());
					}
					return result;
				} else {
					return false;
				}
			} else {
				return uniSameAs(other.origin());
			}
		} else {
			return origin().sameAs(other);
		}
	}

	
}