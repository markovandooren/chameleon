package be.kuleuven.cs.distrinet.chameleon.oo.type.generics;

import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.oo.language.ObjectOrientedLanguage;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;
import be.kuleuven.cs.distrinet.chameleon.util.CreationStackTrace;

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
	protected ExtendsConstraint cloneSelf() {
		return new ExtendsConstraint();
	}


	@Override
	public Type lowerBound() throws LookupException {
		return language(ObjectOrientedLanguage.class).getNullType(view().namespace());
	}


	@Override
	public Type upperBound() throws LookupException {
		return bound();
	}

	@Override
	public TypeReference upperBoundReference() {
		return typeReference();
	}

	@Override
	public String toString() {
		return "extends "+toStringTypeReference();
	}
}
