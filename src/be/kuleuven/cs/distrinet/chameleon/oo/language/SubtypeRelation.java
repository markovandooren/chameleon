package be.kuleuven.cs.distrinet.chameleon.oo.language;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.relation.WeakPartialOrder;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;

public abstract class SubtypeRelation extends WeakPartialOrder<Type> {

	public abstract Type leastUpperBound(List<? extends TypeReference> Us) throws LookupException;
}
