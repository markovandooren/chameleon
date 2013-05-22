package be.kuleuven.cs.distrinet.chameleon.support.statement;

import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;

/**
 * @author Marko van Dooren
 */
public class BreakStatement extends JumpStatement {

	public BreakStatement() {
		super(null);
	}
	
  public BreakStatement(String label) {
    super(label);
  }

  public BreakStatement cloneSelf() {
    return new BreakStatement(getLabel());
  }

	@Override
	public Verification verifySelf() {
		return Valid.create();
//		BreakableStatement ancestor = nearestAncestor(BreakableStatement.class);
//		return checkNull(ancestor, "The break statement is not nested in a breakable statement", Valid.create());
	}

}
