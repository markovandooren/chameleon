package be.kuleuven.cs.distrinet.chameleon.support.statement;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.VerificationResult;

public class DefaultGotoStatement extends GotoStatement {
// FIXME Tim wrote this class. What the heck is it for? Probably something from C#
	@Override
	public DefaultGotoStatement clone() {
		return new DefaultGotoStatement();
	}

	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}
}
