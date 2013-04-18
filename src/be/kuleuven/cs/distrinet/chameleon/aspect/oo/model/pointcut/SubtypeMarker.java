package be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.pointcut;

import be.kuleuven.cs.distrinet.chameleon.core.element.ElementImpl;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;

public class SubtypeMarker extends ElementImpl {

	@Override
	public SubtypeMarker clone() {
		return new SubtypeMarker();
	}

	@Override
	public Verification verifySelf() {
		return Valid.create();
	}

}
