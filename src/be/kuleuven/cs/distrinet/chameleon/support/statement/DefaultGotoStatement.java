package be.kuleuven.cs.distrinet.chameleon.support.statement;

import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;

public class DefaultGotoStatement extends GotoStatement {
// FIXME Tim wrote this class. What the heck is it for? Probably something from C#
	@Override
	public DefaultGotoStatement clone() {
		return new DefaultGotoStatement();
	}

	@Override
	public Verification verifySelf() {
		return Valid.create();
	}
}
