package chameleon.oo.type.generics;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.lookup.LookupException;
import chameleon.oo.language.ObjectOrientedLanguage;
import chameleon.oo.type.Type;
import chameleon.util.CreationStackTrace;

public class SuperWildCardType extends WildCardType {

	private CreationStackTrace _trace = new CreationStackTrace();
	
	public SuperWildCardType(Type lowerBound) throws LookupException {
		super(new SimpleNameSignature("? super "+lowerBound.getName()), lowerBound.language(ObjectOrientedLanguage.class).getDefaultSuperClass(), lowerBound);
	}
	
}
