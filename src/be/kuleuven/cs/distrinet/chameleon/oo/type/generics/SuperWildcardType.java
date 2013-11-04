package be.kuleuven.cs.distrinet.chameleon.oo.type.generics;

import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.oo.language.ObjectOrientedLanguage;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;

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
