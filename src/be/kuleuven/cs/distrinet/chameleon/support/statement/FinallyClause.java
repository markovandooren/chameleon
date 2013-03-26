package be.kuleuven.cs.distrinet.chameleon.support.statement;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.VerificationResult;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.Statement;
import be.kuleuven.cs.distrinet.chameleon.util.Util;

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
