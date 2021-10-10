package org.aikodi.chameleon.support.statement;


import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.validation.BasicProblem;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.expression.Expression;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.oo.statement.Statement;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.util.association.Single;

/**
 * @author Marko van Dooren
 */
public class IfThenElseStatement extends ExpressionContainingStatement {

  public IfThenElseStatement(Expression expression, Statement ifStatement, Statement elseStatement) {
    super(expression);
    setIfStatement(ifStatement);
    setElseStatement(elseStatement);
  }

	/**
	 * IF STATEMENT
	 */
	private Single<Statement> _ifStatement = new Single<Statement>(this);


  public void setIfStatement(Statement statement) {
  	set(_ifStatement, statement);
  }

  public Statement getIfStatement() {
    return _ifStatement.getOtherEnd();
  }

	/**
	 * ELSE STATEMENT
	 */
	private Single<Statement> _elseStatement = new Single<Statement>(this);

  public void setElseStatement(Statement statement) {
    set(_elseStatement, statement);
  }

  public Statement getElseStatement() {
    return _elseStatement.getOtherEnd();
  }


  @Override
protected IfThenElseStatement cloneSelf() {
    return new IfThenElseStatement(null,null,null);
  }

  @Override
  public Verification verifySelf() {
  	Verification result = super.verifySelf();
  	try {
			Type expressionType = getExpression().getType();
			if(! expressionType.subtypeOf(language(ObjectOrientedLanguage.class).booleanType(view().namespace()))) {
				result = result.and(new BasicProblem(getExpression(),"The type of the condition of the if statement is not a boolean type."));
			}
		} catch (LookupException e) {
			result = result.and(new BasicProblem(getExpression(),"Cannot determine the type of the condition of the if statement."));
		}
  	return result;
  }
}
