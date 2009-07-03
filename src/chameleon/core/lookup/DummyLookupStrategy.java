package chameleon.core.lookup;

import chameleon.core.declaration.Declaration;

public class DummyLookupStrategy extends LookupStrategy {

	@Override
	public <T extends Declaration> T lookUp(DeclarationSelector<T> selector) throws LookupException {
		return null;
	}

}
