package org.aikodi.chameleon.core.relation;

import org.aikodi.chameleon.core.lookup.LookupException;

public interface EquivalenceRelation<E> extends org.aikodi.rejuse.logic.relation.EquivalenceRelation<E> {

	@Override
	boolean contains(E first, E second) throws LookupException;

}
