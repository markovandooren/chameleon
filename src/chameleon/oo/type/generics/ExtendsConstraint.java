package chameleon.oo.type.generics;

import chameleon.core.lookup.LookupException;
import chameleon.oo.language.ObjectOrientedLanguage;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeReference;

public class ExtendsConstraint extends TypeConstraint {

	public ExtendsConstraint() {
	}
	
	public ExtendsConstraint(TypeReference ref) {
		setTypeReference(ref);
	}

	@Override
	public boolean matches(Type type) throws LookupException {
		return type.subTypeOf(upperBound());
	}


	@Override
	public ExtendsConstraint cloneThis() {
		return new ExtendsConstraint();
	}


	@Override
	public Type lowerBound() throws LookupException {
		return language(ObjectOrientedLanguage.class).getNullType();
	}


	@Override
	public Type upperBound() throws LookupException {
		return bound();
	}

	@Override
	public TypeReference upperBoundReference() {
		return typeReference();
	}

	
}
