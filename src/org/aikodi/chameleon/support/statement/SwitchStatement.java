package org.aikodi.chameleon.support.statement;

import java.util.List;

import org.aikodi.chameleon.core.validation.BasicProblem;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.expression.Expression;
import org.aikodi.chameleon.util.association.Multi;

/**
 * @author Marko van Dooren
 */
public class SwitchStatement extends ExpressionContainingStatement {

  public SwitchStatement(Expression expression) {
    super(expression);
  }

	/**
	 * SWITCH CASES
	 */
	private Multi<SwitchCase> _switchCases = new Multi<SwitchCase>(this);

  public void addCase(SwitchCase switchCase) {
    add(_switchCases,switchCase);
  }
  
  public void addAllCases(List<SwitchCase> cases) {
  	for(SwitchCase kees: cases) {
  		addCase(kees);
  	}
  }

  public void removeCase(SwitchCase switchCase) {
    remove(_switchCases,switchCase);
  }

  public List<SwitchCase> getSwitchCases() {
    return _switchCases.getOtherEnds();
  }

  @Override
public SwitchStatement cloneSelf() {
    return new SwitchStatement(null);
  }

  @Override
  public Verification verifySelf() {
  	Verification result = super.verifySelf();
  	List<DefaultLabel> cases = lexical().descendants(DefaultLabel.class);
  	if(cases.size() > 1) {
  		result = result.and(new BasicProblem(this,"A switch statement can contain only one default label."));
  	}
  	return result;
  }

}
