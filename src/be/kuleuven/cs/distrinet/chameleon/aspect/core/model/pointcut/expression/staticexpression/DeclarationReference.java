package be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.expression.staticexpression;

import java.util.Collections;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.element.ElementImpl;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.VerificationResult;

public class DeclarationReference extends ElementImpl {

	private String _reference;
	
	public DeclarationReference(String reference) {
		this._reference = reference;
	}
	
	public String reference() {
		return _reference;
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
