package chameleon.core.type.generics;

import chameleon.core.language.ObjectOrientedLanguage;
import chameleon.core.lookup.LookupException;
import chameleon.core.type.Type;
import chameleon.core.type.TypeReference;

public class SuperConstraint extends TypeConstraintWithReferences<SuperConstraint> {

	public SuperConstraint() {
		
	}
	
	public SuperConstraint(TypeReference ref) {
		add(ref);
	}
	
	@Override
	public boolean matches(Type type) throws LookupException {
		return upperBound().subTypeOf(type);
	}


	@Override
	public SuperConstraint cloneThis() {
		return new SuperConstraint();
	}


	@Override
	public Type lowerBound() throws LookupException {
		return bound();
	}


	@Override
	public Type upperBound() throws LookupException {
		return language(ObjectOrientedLanguage.class).getDefaultSuperClass();
	}

}
