package chameleon.core.type.generics;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.language.Language;
import chameleon.core.lookup.LookupException;

public class PureWildCardType extends WildCardType {

	public PureWildCardType(Language language) throws LookupException {
		super(new SimpleNameSignature("?"),language.getDefaultSuperClass(),language.getNullType());
	}

}
