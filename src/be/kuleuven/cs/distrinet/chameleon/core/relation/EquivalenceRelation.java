package be.kuleuven.cs.distrinet.chameleon.core.relation;

import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;

public abstract class EquivalenceRelation<E> extends be.kuleuven.cs.distrinet.rejuse.logic.relation.EquivalenceRelation<E> {

	@Override
	public abstract boolean contains(E first, E second) throws LookupException;

}
