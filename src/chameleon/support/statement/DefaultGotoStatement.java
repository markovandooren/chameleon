package chameleon.support.statement;

import java.util.ArrayList;
import java.util.List;

import chameleon.core.element.Element;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;

public class DefaultGotoStatement extends GotoStatement<DefaultGotoStatement> {
// FIXME Tim wrote this class. What the heck is it for? Probably something from C#
	@Override
	public DefaultGotoStatement clone() {
		return new DefaultGotoStatement();
	}

	public List<Element> children() {
		return new ArrayList<Element>();
	}

	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}
}
