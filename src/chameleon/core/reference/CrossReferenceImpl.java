package chameleon.core.reference;

import chameleon.core.declaration.Declaration;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.DeclaratorSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.namespace.NamespaceElementImpl;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.exception.ChameleonProgrammerException;

public abstract class CrossReferenceImpl<D extends Declaration> extends NamespaceElementImpl implements CrossReference<D> {

	/**
	 * Return the declaration selector that is responsible for selecting the declaration
	 * referenced by this cross-reference.
	 * 
	 * @return
	 */
	public abstract DeclarationSelector<D> selector();

	public abstract CrossReferenceImpl<D> clone();

	public final D getElement() throws LookupException {
		return getElement(selector());
	}
	
	public Declaration getDeclarator() throws LookupException {
		return getElement(new DeclaratorSelector(selector()));
	}

	protected abstract <X extends Declaration > X getElement(DeclarationSelector<X> selector) throws LookupException;
	
	@Override
	public VerificationResult verifySelf() {
		D referencedElement;
		try {
			referencedElement = getElement();
			if(referencedElement != null) {
				return Valid.create();
			} else {
				return new UnresolvableCrossReference(this);
			}
		} catch(LookupException e) {
			return new UnresolvableCrossReference(this, e.getMessage());
		} catch(ChameleonProgrammerException e) {
			return new UnresolvableCrossReference(this, e.getMessage());
		}
	}

  public LookupStrategy targetContext() throws LookupException {
  	return getElement().targetContext();
  }

}
