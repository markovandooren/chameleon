package org.aikodi.chameleon.support.statement;

import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.statement.StatementImpl;

/**
 * @author Marko van Dooren
 */
public class EmptyStatement extends StatementImpl {
	
	public EmptyStatement() {
		
	}
	
	@Override
   protected EmptyStatement cloneSelf() {
		return new EmptyStatement();
	}
	
	@Override
	public Verification verifySelf() {
		return Valid.create();
	}
}
