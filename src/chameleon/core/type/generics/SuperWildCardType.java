package chameleon.core.type.generics;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.lookup.LookupException;
import chameleon.core.type.Type;

public class SuperWildCardType extends WildCardType {

	public SuperWildCardType(Type lowerBound) throws LookupException {
		super(new SimpleNameSignature("? super "+lowerBound.getName()), lowerBound.language().getDefaultSuperClass(), lowerBound);
	}
	
}
