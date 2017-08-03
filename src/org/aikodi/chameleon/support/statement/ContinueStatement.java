package org.aikodi.chameleon.support.statement;

import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;

/**
 * @author Marko van Dooren
 */
public class ContinueStatement extends JumpStatement {

	public ContinueStatement() {
		super(null);
	}
	
  public ContinueStatement(String label) {
    super(label);
  }

  @Override
protected ContinueStatement cloneSelf() {
    return new ContinueStatement(getLabel());
  }

	@Override
	public Verification verifySelf() {
		IterationStatement ancestor = lexical().nearestAncestor(IterationStatement.class);
		return checkNull(ancestor, "The continue statement is not nested in a iteration statement", Valid.create());
	}
}
