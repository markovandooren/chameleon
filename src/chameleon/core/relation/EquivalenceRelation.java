package chameleon.core.relation;

import chameleon.core.lookup.LookupException;

public abstract class EquivalenceRelation<E> extends org.rejuse.logic.relation.EquivalenceRelation<E> {

	@Override
	public abstract boolean contains(E first, E second) throws LookupException;

}
