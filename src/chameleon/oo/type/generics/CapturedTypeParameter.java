package chameleon.oo.type.generics;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.oo.type.DerivedType;
import chameleon.oo.type.NonLocalTypeReference;
import chameleon.oo.type.Type;

public class CapturedTypeParameter extends FormalTypeParameter {
//FIXME a captured type parameter should NOT be a formal type parameter but an instantiated type parameter!!!!
//      I must modify the instantiated type parameter hierarchy.
	public CapturedTypeParameter(SimpleNameSignature signature) {
		super(signature);
	}

	@Override
	public CapturedTypeParameter clone() {
		CapturedTypeParameter result = new CapturedTypeParameter(signature().clone());
		for(TypeConstraint constraint: constraints()) {
			result.addConstraint(constraint.clone());
		}
		for(NonLocalTypeReference nl: result.descendants(NonLocalTypeReference.class)) {
			Element p = nl.lookupParent();
			if(p == this || p.ancestors().contains(this)) {
				nl.setLookupParent(result);
			}
		}
		return result;
	}
	
	@Override
	protected Type createLazyAlias() {
		return new LazyInstantiatedAlias(signature().clone(), this);
	}

	@Override
	protected synchronized Type createSelectionType() throws LookupException {
//		String x = nearestAncestor(Type.class).getFullyQualifiedName() +"."+ signature();
//		if(x.equals("chameleon.core.member.Member.E")) {
//			System.out.println("Creating selection type of " + x);
//		}
		if(_selectionTypeCache == null) {
		  _selectionTypeCache = new InstantiatedParameterType(signature().clone(), upperBound(),this);
		}
		return _selectionTypeCache;
	}
	
	@Override
	public synchronized void flushLocalCache() {
		super.flushLocalCache();
		_selectionTypeCache = null;
	}

	private Type _selectionTypeCache;

//	@Override
//	public CapturedTypeParameter cloneForStub() throws LookupException {
//		CapturedTypeParameter result = clone();
//		for(NonLocalTypeReference nl: result.descendants(NonLocalTypeReference.class)) {
//			Element p = nl.lookupParent();
//			if(p.sameAs(this) || p.ancestors().contains(this)) {
//				nl.setLookupParent(result);
//			}
//		}
//		return result;
//	}

//	@Override
//	public boolean uniSameAs(Element other) throws LookupException {
//		if(origin() == this) {
//			if(other == other.origin()) {
//			  // The real test is here.
//				if(other instanceof CapturedTypeParameter) {
//					boolean result = signature().sameAs(((CapturedTypeParameter) other).signature());
//					if(result) {
//					  result = nearestAncestor(DerivedType.class).baseType().sameAs(((DerivedType)other.nearestAncestor(DerivedType.class)).baseType());
//					}
//					return result;
//				} else {
//					return false;
//				}
//			} else {
//				return uniSameAs(other.origin());
//			}
//		} else {
//			return origin().sameAs(other);
//		}
//	}

	
	
}