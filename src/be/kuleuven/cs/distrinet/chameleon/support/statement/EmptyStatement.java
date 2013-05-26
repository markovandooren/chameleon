package be.kuleuven.cs.distrinet.chameleon.support.statement;

import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.StatementImpl;

/**
 * @author Marko van Dooren
 */
public class EmptyStatement extends StatementImpl {
	
	public EmptyStatement() {
		
	}
	
	protected EmptyStatement cloneSelf() {
		return new EmptyStatement();
	}
	
	@Override
	public Verification verifySelf() {
		return Valid.create();
	}
}
