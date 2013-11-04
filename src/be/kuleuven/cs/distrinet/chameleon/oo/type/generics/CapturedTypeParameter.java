package be.kuleuven.cs.distrinet.chameleon.oo.type.generics;

import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;

public class CapturedTypeParameter extends FormalTypeParameter {
//FIXME a captured type parameter should NOT be a formal type parameter but an instantiated type parameter!!!!
//      I must modify the instantiated type parameter hierarchy.
	public CapturedTypeParameter(String name) {
		super(name);
	}

	@Override
	protected CapturedTypeParameter cloneSelf() {
		return new CapturedTypeParameter(name());
	}
	
	@Override
	protected Type createLazyAlias() {
		return new LazyInstantiatedAlias(name(), this);
	}

	@Override
	protected synchronized Type createSelectionType() throws LookupException {
//		String x = nearestAncestor(Type.class).getFullyQualifiedName() +"."+ signature();
//		if(x.equals("chameleon.core.member.Member.E")) {
//			System.out.println("Creating selection type of " + x);
//		}
		if(_selectionTypeCache == null) {
		  _selectionTypeCache = new InstantiatedParameterType(name(), upperBound(),this);
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
