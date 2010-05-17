package chameleon.oo.type.generics;

import chameleon.core.lookup.LookupException;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeReference;

public class EqualityConstraint extends TypeConstraint<EqualityConstraint> {

	public EqualityConstraint() {
	}
	
	public EqualityConstraint(TypeReference ref) {
		setTypeReference(ref);
	}

	@Override
	public EqualityConstraint cloneThis() {
		return new EqualityConstraint();
	}

	@Override
	public Type lowerBound() throws LookupException {
		return bound();
	}

	@Override
	public boolean matches(Type type) throws LookupException {
		return type.sameAs(bound());
	}

	@Override
	public Type upperBound() throws LookupException {
//		String t = nearestAncestor(Type.class).getFullyQualifiedName();
//		String p = nearestAncestor(TypeParameter.class).signature().name();
//		String x = t+"."+p;
//		if(x.equals("java.util.AbstractCollection.E")) {
//			System.out.println(x);
//		}
		return bound();
	}

	@Override
	public TypeReference upperBoundReference() {
		return typeReference();
	}

}
