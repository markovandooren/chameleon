package be.kuleuven.cs.distrinet.chameleon.support.statement;


import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.validation.BasicProblem;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;
import be.kuleuven.cs.distrinet.chameleon.oo.language.ObjectOrientedLanguage;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.Statement;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

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
			if(! expressionType.subTypeOf(language(ObjectOrientedLanguage.class).booleanType(view().namespace()))) {
				result = result.and(new BasicProblem(getExpression(),"The type of the condition of the if statement is not a boolean type."));
			}
		} catch (LookupException e) {
			result = result.and(new BasicProblem(getExpression(),"Cannot determine the type of the condition of the if statement."));
		}
  	return result;
  }
}
