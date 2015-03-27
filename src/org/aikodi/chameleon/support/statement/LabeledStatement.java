package org.aikodi.chameleon.support.statement;

import org.aikodi.chameleon.core.event.name.NameChanged;
import org.aikodi.chameleon.core.validation.BasicProblem;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.statement.Statement;

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
    String old = _label;
    _label = label;
    if(changeNotificationEnabled()) {
      notify(new NameChanged(old, label));
    }
  }

  @Override
protected LabeledStatement cloneSelf() {
    return new LabeledStatement(getLabel(), null);
  }

	@Override
	public Verification verifySelf() {
		Verification result = Valid.create();
		if(getLabel() == null) {
			result = result.and(new BasicProblem(this,"The label is missing."));
		}
		return result;
	}
}
