package be.kuleuven.cs.distrinet.chameleon.support.statement;

import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.validation.BasicProblem;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;
import be.kuleuven.cs.distrinet.chameleon.oo.method.Method;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;

/**
 * @author Marko van Dooren
 */
public class ReturnStatement extends ExpressionContainingStatement {

	public ReturnStatement() {
		
	}
	
  /**
   * @param expression
   */
  public ReturnStatement(Expression expression) {
    super(expression);
  }

  protected ReturnStatement cloneSelf() {
    return new ReturnStatement(null);
  }
  
  @Override
  public Verification verifySelf() {
  	Verification result = Valid.create();
  	try {
			Method nearestAncestor = nearestAncestor(Method.class);
			if(nearestAncestor != null) {
				Expression expression = getExpression();
				if(expression != null) {
			    Type returnType = nearestAncestor.returnType();
			    // problem with the type of the return value will be reported by the expression.
			    Type type = expression.getType();
			    try {
			    	if(! type.subTypeOf(returnType)) {
			    		result = result.and(new BasicProblem(this, "The type of the return value is not a subtype of the return type of the method."));
			    	}
			    }catch (LookupException e) {
						result = result.and(new BasicProblem(this, "Cannot determine the relation between the type of the return value and the return type of the method."));
					}
				}
			} else {
				result = result.and(new BasicProblem(this, "The return statement is not contained in a method."));
			}
		} catch (LookupException e) {
			// If there is a return type, but it cannot be resolved, that crossreference will report the problem.
		}
  	return result;
  }


}
