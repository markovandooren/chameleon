package chameleon.oo.type.generics;

import chameleon.core.lookup.LookupException;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.language.ObjectOrientedLanguage;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeReference;

public class SuperConstraint extends TypeConstraintWithReferences<SuperConstraint> {

	public SuperConstraint() {
		
	}
	
	public SuperConstraint(TypeReference ref) {
		setTypeReference(ref);
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

	@Override
	public TypeReference upperBoundReference() {
		ObjectOrientedLanguage language = language(ObjectOrientedLanguage.class);
		return language.createTypeReference(language.getDefaultSuperClassFQN());
	}

}
