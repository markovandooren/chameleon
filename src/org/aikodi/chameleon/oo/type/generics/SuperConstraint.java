package org.aikodi.chameleon.oo.type.generics;

import java.util.Set;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeReference;

public class SuperConstraint extends TypeConstraint {

	private SuperConstraint() {
		
	}
	
	public SuperConstraint(TypeReference ref) {
		setTypeReference(ref);
	}
	
//	@Override
//	public boolean matches(Type type) throws LookupException {
//		return upperBound().subTypeOf(type);
//	}

	@Override
	protected SuperConstraint cloneSelf() {
		return new SuperConstraint();
	}

	@Override
	public Type lowerBound() throws LookupException {
		return bound();
	}

	@Override
	public Type upperBound() throws LookupException {
		return language(ObjectOrientedLanguage.class).getDefaultSuperClass(view().namespace());
	}

	@Override
	public TypeReference upperBoundReference() {
		ObjectOrientedLanguage language = language(ObjectOrientedLanguage.class);
		return language.createTypeReference(language.getDefaultSuperClassFQN());
	}

	@Override
	public String toString(Set<Element> visited) {
		return "super "+toStringTypeReference(visited);
	}

	public TypeArgument argument() {
		SuperWildcard result = language(ObjectOrientedLanguage.class).createSuperWildcard(clone(typeReference()));
		result.setUniParent(this);
		return result;
	}

}
