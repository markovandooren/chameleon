package chameleon.util.association;

import be.kuleuven.cs.distrinet.rejuse.association.IAssociation;
import chameleon.core.element.Element;
import chameleon.core.validation.VerificationResult;

public interface ChameleonAssociation<T> extends IAssociation<Element, T> {

	public void cloneTo(ChameleonAssociation<T> o);
	
	public VerificationResult verify();
}
