package be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.pointcut.method;

import be.kuleuven.cs.distrinet.chameleon.core.element.ElementImpl;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.VerificationResult;

public class AnnotationReference extends ElementImpl {
	
	private String referencedName;
	
	public AnnotationReference(String reference) {
		setReferencendName(reference);
	}
	
	public String referencendName() {
		return referencedName;
	}

	private void setReferencendName(String reference) {
		this.referencedName = reference;
	}

	@Override
	public AnnotationReference clone() {
		return new AnnotationReference(referencendName());
	}

	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}
}
