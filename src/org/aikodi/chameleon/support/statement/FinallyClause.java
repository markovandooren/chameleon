package org.aikodi.chameleon.support.statement;

import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.statement.Statement;

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
  @Override
protected FinallyClause cloneSelf() {
    return new FinallyClause(null);
  }

	@Override
	public Verification verifySelf() {
		return Valid.create();
	}
}
