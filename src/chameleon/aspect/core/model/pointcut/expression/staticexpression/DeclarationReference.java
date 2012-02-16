package chameleon.aspect.core.model.pointcut.expression.staticexpression;

import java.util.Collections;
import java.util.List;

import chameleon.core.element.Element;
import chameleon.core.element.ElementImpl;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;

public class DeclarationReference extends ElementImpl {

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
	public DeclarationReference clone() {
		return new DeclarationReference(_reference);
	}

	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}

}
