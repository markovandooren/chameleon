package org.aikodi.chameleon.aspect.oo.model.pointcut;

import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;

public class SubtypeMarker extends ElementImpl {

	@Override
	protected SubtypeMarker cloneSelf() {
		return new SubtypeMarker();
	}

	@Override
	public Verification verifySelf() {
		return Valid.create();
	}

}
