package be.kuleuven.cs.distrinet.chameleon.oo.member;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Signature;

public abstract class HidesRelation<D extends Declaration> extends DeclarationComparator<D> {

	public HidesRelation(Class<D> kind) {
		super(kind);
	}
	
	@Override
	public boolean containsBasedOnName(Signature first, Signature second) {
		return first.name().equals(second.name());
	}

}
