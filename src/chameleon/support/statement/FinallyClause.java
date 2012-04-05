package chameleon.support.statement;

import java.util.List;

import chameleon.core.element.Element;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.statement.Statement;
import chameleon.util.Util;

/**
 * A class of finally clauses for a try statement.
 * 
 * @author Marko van Dooren
 */
public class FinallyClause extends Clause {

  public FinallyClause(Statement statement) {
    super(statement);
  }


  /**
   * @return
   */
  public FinallyClause clone() {
    return new FinallyClause(statement().clone());
  }

	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}
}
