package be.kuleuven.cs.distrinet.chameleon.support.statement;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.VerificationResult;

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

  public BreakStatement clone() {
    return new BreakStatement(getLabel());
  }

	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
//		BreakableStatement ancestor = nearestAncestor(BreakableStatement.class);
//		return checkNull(ancestor, "The break statement is not nested in a breakable statement", Valid.create());
	}

}
