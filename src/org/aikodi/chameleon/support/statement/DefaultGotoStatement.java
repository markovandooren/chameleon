package org.aikodi.chameleon.support.statement;

import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;

public class DefaultGotoStatement extends GotoStatement {
// FIXME Tim wrote this class. What the heck is it for? Probably something from C#
	@Override
	protected DefaultGotoStatement cloneSelf() {
		return new DefaultGotoStatement();
	}

	@Override
	public Verification verifySelf() {
		return Valid.create();
	}
}
