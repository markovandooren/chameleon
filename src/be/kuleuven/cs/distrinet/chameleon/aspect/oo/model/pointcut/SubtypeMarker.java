package be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.pointcut;

import java.util.Collections;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.element.ElementImpl;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.VerificationResult;

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
