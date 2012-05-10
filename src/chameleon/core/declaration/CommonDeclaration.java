package chameleon.core.declaration;

import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.util.association.Single;

public abstract class CommonDeclaration extends DeclarationImpl {

	public CommonDeclaration() {
	}
	
	public CommonDeclaration(Signature signature) {
		setSignature(signature);
	}

	private Single<Signature> _signature = new Single<Signature>(this);

	@Override
	public Signature signature() {
		return _signature.getOtherEnd();
	}

	@Override
	public void setSignature(Signature signature) {
		set(_signature,signature);
	}
	
	@Override
	public LookupStrategy targetContext() throws LookupException {
		throw new LookupException("A goal does not contain declarations.");
	}

}