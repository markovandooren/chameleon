package be.kuleuven.cs.distrinet.chameleon.support.statement;

import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.VerificationResult;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.StatementImpl;

/**
 * @author Marko van Dooren
 */
public class EmptyStatement extends StatementImpl {
	
	public EmptyStatement() {
		
	}
	
	public EmptyStatement clone() {
		return new EmptyStatement();
	}
	
	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}
}