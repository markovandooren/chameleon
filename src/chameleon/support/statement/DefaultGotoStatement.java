package chameleon.support.statement;

import java.util.ArrayList;
import java.util.List;

import chameleon.core.element.Element;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;

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
