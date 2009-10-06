package chameleon.core.type.generics;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.lookup.LookupException;
import chameleon.oo.language.ObjectOrientedLanguage;

public class PureWildCardType extends WildCardType {

	public PureWildCardType(ObjectOrientedLanguage language) throws LookupException {
		super(new SimpleNameSignature("?"),language.getDefaultSuperClass(),language.getNullType());
	}

}
