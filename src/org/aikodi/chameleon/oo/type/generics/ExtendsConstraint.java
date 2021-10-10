package org.aikodi.chameleon.oo.type.generics;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeReference;

import java.util.Set;

public class ExtendsConstraint extends TypeConstraint {

	public ExtendsConstraint() {
	}
	
	public ExtendsConstraint(TypeReference ref) {
		setTypeReference(ref);
	}

//	@Override
//	public boolean matches(Type type) throws LookupException {
//		return type.subTypeOf(upperBound());
//	}


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
	public String toString(Set<Element> visited) {
		return "extends "+toStringTypeReference(visited);
	}
	
	public TypeArgument argument() {
		ExtendsWildcard result = language(ObjectOrientedLanguage.class).createExtendsWildcard(clone(typeReference()));
		result.setUniParent(this);
		return result;
	}

}
