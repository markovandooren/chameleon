package org.aikodi.chameleon.aspect.core.model.pointcut.expression.staticexpression;

import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;

public class DeclarationReference extends ElementImpl {

	private String _reference;
	
	public DeclarationReference(String reference) {
		this._reference = reference;
	}
	
	public String reference() {
		return _reference;
	}
	
	@Override
	public DeclarationReference cloneSelf() {
		return new DeclarationReference(_reference);
	}

	@Override
	public Verification verifySelf() {
		return Valid.create();
	}

}
