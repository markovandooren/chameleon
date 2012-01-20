package chameleon.support.statement;

import java.util.List;

import chameleon.core.element.Element;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.statement.Statement;
import chameleon.util.Util;

/**
 * @author Marko van Dooren
 */
public class LabeledStatement extends StatementContainingStatement<LabeledStatement> {

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

  public List<Element> children() {
    return Util.createNonNullList(getStatement());
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
