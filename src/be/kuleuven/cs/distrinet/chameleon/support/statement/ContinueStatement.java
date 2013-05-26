package be.kuleuven.cs.distrinet.chameleon.support.statement;

import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;

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

  protected ContinueStatement cloneSelf() {
    return new ContinueStatement(getLabel());
  }

	@Override
	public Verification verifySelf() {
		IterationStatement ancestor = nearestAncestor(IterationStatement.class);
		return checkNull(ancestor, "The continue statement is not nested in a iteration statement", Valid.create());
	}
}
