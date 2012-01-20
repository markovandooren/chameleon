package chameleon.aspect.core.model.pointcut.expression.staticexpression;

import java.util.Collections;
import java.util.List;

import chameleon.core.element.Element;
import chameleon.core.element.ElementImpl;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;

public class DeclarationReference<E extends DeclarationReference<E>> extends ElementImpl<E> {

	private String _reference;
	
	public DeclarationReference(String reference) {
		this._reference = reference;
	}
	
	public String reference() {
		return _reference;
	}
	
	@Override
	public List<? extends Element> children() {
		return Collections.emptyList();
	}

	@Override
	public E clone() {
		return (E) new DeclarationReference(_reference);
	}

	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}

}
