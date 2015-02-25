package org.aikodi.chameleon.oo.language;

import java.util.List;

import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.relation.WeakPartialOrder;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeReference;

public abstract class SubtypeRelation extends WeakPartialOrder<Type> {

	public abstract Type leastUpperBound(List<? extends TypeReference> Us) throws LookupException;
}
