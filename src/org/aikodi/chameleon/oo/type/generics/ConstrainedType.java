package org.aikodi.chameleon.oo.type.generics;

import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.oo.type.ConstrainedTypeReference;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeReference;

public class ConstrainedType extends IntervalType {

	public ConstrainedType(Type lowerBound, Type upperBound) {
		super("? super "+lowerBound.name()+" extends "+upperBound.name(), lowerBound, upperBound);
	}

	@Override
	protected ConstrainedType cloneSelf() {
		return new ConstrainedType(lowerBound(), upperBound());
	}

	@Override
	public String getFullyQualifiedName() {
		return "? super "+lowerBound().getFullyQualifiedName()+" extends "+upperBound().getFullyQualifiedName();
	}

	public TypeReference reference() {
		ObjectOrientedLanguage language = language(ObjectOrientedLanguage.class);
		ConstrainedTypeReference result = language.createConstrainedTypeReference();;

		Type up = upperBound();
		TypeReference upRef = language.reference(up);
		upRef.setUniParent(null);
		Type low = lowerBound();
		TypeReference lowRef = language.reference(low);
		lowRef.setUniParent(null);
		result.addConstraint(new ExtendsConstraint(upRef));
		result.addConstraint(new SuperConstraint(lowRef));
		result.setUniParent(view().namespace());
		return result;
	}

}
