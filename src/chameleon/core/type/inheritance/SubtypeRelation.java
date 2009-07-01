package chameleon.core.type.inheritance;

import chameleon.core.MetamodelException;
import chameleon.core.lookup.LookupException;
import chameleon.core.type.Type;
import chameleon.core.type.TypeReference;

public class SubtypeRelation extends InheritanceRelation<SubtypeRelation> {

	public SubtypeRelation(TypeReference ref) {
		super(ref);
	}

	@Override
	public SubtypeRelation clone() {
		return new SubtypeRelation(superClassReference().clone());
	}

	@Override
	public Type superType() throws LookupException {
		return superClass();
	}

}
