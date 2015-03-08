package org.aikodi.chameleon.oo.type.generics;

import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.oo.type.Type;

public class SuperWildcardType extends WildCardType {

	public SuperWildcardType(Type lowerBound) throws LookupException {
		super("? super "+lowerBound.name(), lowerBound.language(ObjectOrientedLanguage.class).getDefaultSuperClass(lowerBound.view().namespace()), lowerBound);
	}
	
	public Type bound() {
		return lowerBound();
	}
	
	@Override
	public String getFullyQualifiedName() {
		return "? super "+lowerBound().getFullyQualifiedName();
	}


}
