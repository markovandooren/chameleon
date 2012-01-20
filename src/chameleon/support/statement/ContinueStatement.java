package chameleon.support.statement;

import java.util.ArrayList;
import java.util.List;

import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;

/**
 * @author Marko van Dooren
 */
public class ContinueStatement extends JumpStatement<ContinueStatement> {

	public ContinueStatement() {
		super(null);
	}
	
  public ContinueStatement(String label) {
    super(label);
  }

  public ContinueStatement clone() {
    return new ContinueStatement(getLabel());
  }

  public List children() {
    return new ArrayList();
  }

	@Override
	public VerificationResult verifySelf() {
		IterationStatement ancestor = nearestAncestor(IterationStatement.class);
		return checkNull(ancestor, "The continue statement is not nested in a iteration statement", Valid.create());
	}
}
