package org.aikodi.chameleon.oo.type.generics;

import java.util.Set;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeReference;

public class EqualityConstraint extends TypeConstraint {
	
	public EqualityConstraint() {
	}
	
	public EqualityConstraint(TypeReference ref) {
		setTypeReference(ref);
	}

	@Override
	protected EqualityConstraint cloneSelf() {
		return new EqualityConstraint();
	}

	@Override
	public Type lowerBound() throws LookupException {
		return bound().lowerBound();
	}

	@Override
	public Type upperBound() throws LookupException {
		return bound().upperBound();
	}

	@Override
	public TypeReference upperBoundReference() {
		return typeReference();
	}

	@Override
	public String toString(Set<Element> visited) {
		return "equals "+toStringTypeReference(visited);
	}

	public TypeArgument argument() {
		EqualityTypeArgument result = language(ObjectOrientedLanguage.class).createEqualityTypeArgument(clone(typeReference()));
		result.setUniParent(this);
		return result;
	}

}
