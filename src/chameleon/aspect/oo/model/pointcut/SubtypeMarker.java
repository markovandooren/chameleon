package chameleon.aspect.oo.model.pointcut;

import java.util.Collections;
import java.util.List;

import chameleon.core.element.Element;
import chameleon.core.element.ElementImpl;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;

public class SubtypeMarker extends ElementImpl {

	@Override
	public SubtypeMarker clone() {
		return new SubtypeMarker();
	}

	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}

}
