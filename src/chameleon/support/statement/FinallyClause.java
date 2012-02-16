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

 /*@
   @ also public behavior
   @
   @ post getExpression() != null ==> \result.contains(getExpression());
   @ post \result.size() == 1;
   @*/
  public List<Element> children() {
    return Util.createNonNullList(statement());
  }


	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}
}
