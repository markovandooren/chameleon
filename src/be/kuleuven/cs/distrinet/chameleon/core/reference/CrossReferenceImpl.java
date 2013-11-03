package be.kuleuven.cs.distrinet.chameleon.core.reference;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.element.ElementImpl;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclaratorSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;

public abstract class CrossReferenceImpl<D extends Declaration> extends ElementImpl implements CrossReference<D> {

	/**
	 * Return the declaration selector that is responsible for selecting the declaration
	 * referenced by this cross-reference.
	 * 
	 * @return
	 */
	public abstract DeclarationSelector<D> selector();

	public D getElement() throws LookupException {
		return getElement(selector());
	}
	
	public Declaration getDeclarator() throws LookupException {
		return getElement(new DeclaratorSelector(selector()));
	}

	protected abstract <X extends Declaration > X getElement(DeclarationSelector<X> selector) throws LookupException;
	
	@Override
	public Verification verifySelf() {
		try {
			return getElement() != null ?
				Valid.create() : new UnresolvableCrossReference(this);
		} catch(LookupException e) {
			return new UnresolvableCrossReference(this, e.getMessage());
		} catch(ChameleonProgrammerException e) {
			return new UnresolvableCrossReference(this, e.getMessage());
		}
	}

  public LookupContext targetContext() throws LookupException {
  	return getElement().targetContext();
  }

}
