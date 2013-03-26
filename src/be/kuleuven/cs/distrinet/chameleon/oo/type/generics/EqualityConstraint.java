package be.kuleuven.cs.distrinet.chameleon.oo.type.generics;

import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;

public class EqualityConstraint extends TypeConstraint {
	
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
		return bound().lowerBound();
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
		return bound().upperBound();
	}

	@Override
	public TypeReference upperBoundReference() {
		return typeReference();
	}

}
