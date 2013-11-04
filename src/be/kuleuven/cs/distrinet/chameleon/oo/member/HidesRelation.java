package be.kuleuven.cs.distrinet.chameleon.oo.member;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;

public class HidesRelation<D extends Declaration> extends DeclarationComparator<D> {

	public HidesRelation(Class<D> kind) {
		super(kind);
	}
	
}
