package be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.advice;

import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.validation.BasicProblem;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.support.statement.ReturnStatement;

public class AdviceReturnStatement extends ReturnStatement {
	public AdviceReturnStatement() {
		
	}
	
	public AdviceReturnStatement(Expression expression) {
		super(expression);
	}

	@Override
	  public Verification verifySelf() {
	  	Verification result = Valid.create();
	  	
	  	try {
	  		ProgrammingAdvice nearestAncestor = nearestAncestor(ProgrammingAdvice.class);
				if(nearestAncestor != null) {
					Expression expression = getExpression();
					if(expression != null) {
				    Type returnType = nearestAncestor.actualReturnType();
				    // problem with the type of the return value will be reported by the expression.
				    Type type = expression.getType();
				    try {
				    	if(! type.subTypeOf(returnType)) {
				    		result = result.and(new BasicProblem(this, "The type of the return value is not a subtype of the return type of the advice."));
				    	}
				    }catch (LookupException e) {
							result = result.and(new BasicProblem(this, "Cannot determine the relation between the type of the return value and the return type of the advice."));
						}
					}
				} else {
					result = result.and(new BasicProblem(this, "The return statement is not contained in an advice."));
				}
			} catch (LookupException e) {
				// If there is a return type, but it cannot be resolved, that crossreference will report the problem.
			}
	  	return result;
	  }
	  
	  @Override
   protected AdviceReturnStatement cloneSelf() {
		    return new AdviceReturnStatement(null);
		  }
}
