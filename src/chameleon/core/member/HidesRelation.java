package chameleon.core.member;

import chameleon.core.declaration.Declaration;

public abstract class HidesRelation<D extends Declaration<?,?,?,?>> extends DeclarationComparator<D> {

	public HidesRelation(Class<D> kind) {
		super(kind);
	}
	
	@Override
	public boolean containsBasedOnName(D first, D second) {
		return first.signature().name().equals(second.signature().name());
	}

}
