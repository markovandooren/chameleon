package chameleon.core.reference;

import chameleon.core.declaration.Declaration;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.DeclaratorSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespace.NamespaceElementImpl;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;

public abstract class CrossReferenceImpl<E extends CrossReference, P extends Element, D extends Declaration> extends NamespaceElementImpl<E,P> implements CrossReference<E, P, D> {

	public final D getElement() throws LookupException {
		return getElement(selector());
	}
	
	public Declaration getDeclarator() throws LookupException {
		return getElement(new DeclaratorSelector(selector()));
	}

	protected abstract <X extends Declaration > X getElement(DeclarationSelector<X> selector) throws LookupException;
	
	@Override
	public VerificationResult verifyThis() {
		D referencedElement;
		try {
			referencedElement = getElement();
			if(referencedElement != null) {
				return Valid.create();
			} else {
				return new UnresolvableCrossReference(this);
			}
		} catch (LookupException e) {
			return new UnresolvableCrossReference(this);
		}
	}

}
