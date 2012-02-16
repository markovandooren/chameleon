package chameleon.support.statement;

import java.util.ArrayList;
import java.util.List;

import chameleon.core.element.Element;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.statement.StatementImpl;

/**
 * @author Marko van Dooren
 */
public class EmptyStatement extends StatementImpl {
	
	public EmptyStatement() {
		
	}
	
	public EmptyStatement clone() {
		return new EmptyStatement();
	}
	
	public List<Element> children() {
		return new ArrayList();
	}

	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}
}
