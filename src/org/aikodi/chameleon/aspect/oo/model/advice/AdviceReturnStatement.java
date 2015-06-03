package org.aikodi.chameleon.aspect.oo.model.advice;

import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.validation.BasicProblem;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.expression.Expression;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.support.statement.ReturnStatement;

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
				    	if(! type.subtypeOf(returnType)) {
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
