package org.aikodi.chameleon.aspect.oo.model.pointcut.method;

import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;

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
	public AnnotationReference cloneSelf() {
		return new AnnotationReference(referencendName());
	}

	@Override
	public Verification verifySelf() {
		return Valid.create();
	}
}
