package chameleon.support.statement;

import java.util.ArrayList;
import java.util.List;

import org.rejuse.association.SingleAssociation;

import chameleon.core.element.Element;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.expression.Expression;
import chameleon.util.Util;

/**
 * @author Marko van Dooren
 */
public class CaseLabel extends SwitchLabel<CaseLabel> {
  
  public CaseLabel(Expression expr) {
    setExpression(expr);
  }

	/**
	 * EXPRESSION
	 */
	private SingleAssociation<CaseLabel,Expression> _expression = new SingleAssociation<CaseLabel,Expression>(this);

  
  public Expression<? extends Expression> getExpression() {
    return _expression.getOtherEnd();
  }
  
  public void setExpression(Expression expression) {
    _expression.connectTo(expression.parentLink());
  }

  public void removeExpression(Expression expr) {
    if(getExpression() == expr) {
      _expression.connectTo(null);
    }
  }

  public CaseLabel clone() {
    return new CaseLabel(getExpression().clone());
  }

  public List<Element> children() {
    return Util.createNonNullList(getExpression());
  }

	@Override
	public VerificationResult verifySelf() {
		return checkNull(getExpression(), "The case label has no expression", Valid.create());
	}

}
