package org.aikodi.chameleon.oo.type.generics;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.oo.type.Type;

public class ExtendsWildcardType extends IntervalType {

	public ExtendsWildcardType(Type upperBound) {
		super("? extends "+upperBound.name(),upperBound.language(ObjectOrientedLanguage.class).getNullType(upperBound.view().namespace()),upperBound);
	}

	public Type bound() {
		return upperBound();
	}
	
	@Override
	protected Element cloneSelf() {
	  return new ExtendsWildcardType(upperBound());
	}

	@Override
	public String getFullyQualifiedName() {
		return "? extends "+upperBound().getFullyQualifiedName();
	}


}
