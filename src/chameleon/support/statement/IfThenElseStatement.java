package chameleon.support.statement;


import chameleon.core.lookup.LookupException;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.expression.Expression;
import chameleon.oo.language.ObjectOrientedLanguage;
import chameleon.oo.statement.Statement;
import chameleon.oo.type.Type;
import chameleon.util.association.Single;

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


  public IfThenElseStatement clone() {
    Statement ifStatement = null;
    if(getIfStatement() != null) {
      ifStatement = getIfStatement().clone();
    }
    Statement elseStatement = null;
    if(getElseStatement() != null) {
      elseStatement = getElseStatement().clone();
    }
    return new IfThenElseStatement(getExpression().clone(), ifStatement, elseStatement);
  }

  @Override
  public VerificationResult verifySelf() {
  	VerificationResult result = super.verifySelf();
  	try {
			Type expressionType = getExpression().getType();
			if(! expressionType.subTypeOf(language(ObjectOrientedLanguage.class).booleanType())) {
				result = result.and(new BasicProblem(getExpression(),"The type of the condition of the if statement is not a boolean type."));
			}
		} catch (LookupException e) {
			result = result.and(new BasicProblem(getExpression(),"Cannot determine the type of the condition of the if statement."));
		}
  	return result;
  }
}
