package org.aikodi.chameleon.oo.type.generics;

import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.oo.type.Type;

public class ExtendsWildcardType extends WildCardType {

	public ExtendsWildcardType(Type upperBound) {
		super("? extends "+upperBound.name(), upperBound,upperBound.language(ObjectOrientedLanguage.class).getNullType(upperBound.view().namespace()));
	}

	public Type bound() {
		return upperBound();
	}

	@Override
	public String getFullyQualifiedName() {
		return "? extends "+upperBound().getFullyQualifiedName();
	}


}
