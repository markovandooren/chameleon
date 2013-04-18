package be.kuleuven.cs.distrinet.chameleon.support.statement;

import be.kuleuven.cs.distrinet.chameleon.core.validation.BasicProblem;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.VerificationResult;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.Statement;

/**
 * @author Marko van Dooren
 */
public class LabeledStatement extends StatementContainingStatement {

  public LabeledStatement(String label, Statement statement) {
    super(statement);
    setLabel(label);
  }

  private String _label;

  public String getLabel() {
    return _label;
  }

  public void setLabel(String label) {
    _label = label;
  }

  public LabeledStatement clone() {
    return new LabeledStatement(getLabel(), getStatement().clone());
  }

	@Override
	public VerificationResult verifySelf() {
		VerificationResult result = Valid.create();
		if(getLabel() == null) {
			result = result.and(new BasicProblem(this,"The label is missing."));
		}
		return result;
	}
}
