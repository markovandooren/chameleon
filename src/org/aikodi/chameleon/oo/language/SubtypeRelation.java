package org.aikodi.chameleon.oo.language;

import java.util.List;

import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeReference;

public abstract class SubtypeRelation {

	/**
	 * Compute the least upper bound of the types referenced by the given
	 * list of type references.
	 * 
	 * @param typeReferences A list of type references that refer to the types for
	 *                       which the least upper bound must be computed. The list
	 *                       cannot be null or contain null.
	 * @return
	 * @throws LookupException
	 */
	public abstract Type leastUpperBound(List<? extends TypeReference> typeReferences) throws LookupException;
}
