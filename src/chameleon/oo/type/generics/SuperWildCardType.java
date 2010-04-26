package chameleon.oo.type.generics;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.lookup.LookupException;
import chameleon.oo.language.ObjectOrientedLanguage;
import chameleon.oo.type.Type;

public class SuperWildCardType extends WildCardType {

	public SuperWildCardType(Type lowerBound) throws LookupException {
		super(new SimpleNameSignature("? super "+lowerBound.getName()), lowerBound.language(ObjectOrientedLanguage.class).getDefaultSuperClass(), lowerBound);
	}
	
}
