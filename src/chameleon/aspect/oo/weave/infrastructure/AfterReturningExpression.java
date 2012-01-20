package chameleon.aspect.oo.weave.infrastructure;

import chameleon.aspect.oo.model.advice.ProgrammingAdvice;
import chameleon.aspect.oo.model.advice.modifier.Returning;
import chameleon.aspect.oo.model.language.AspectOrientedOOLanguage;
import chameleon.core.lookup.LookupException;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.exception.ModelException;
import chameleon.oo.expression.NamedTargetExpression;
import chameleon.oo.statement.Block;
import chameleon.oo.variable.VariableDeclaration;
import chameleon.support.statement.ReturnStatement;
import chameleon.support.variable.LocalVariable;
import chameleon.support.variable.LocalVariableDeclarator;

public class AfterReturningExpression extends AdvisedExpressionFactory {
	
	public AfterReturningExpression(ExpressionInfrastructureFactory factory) {
		super(factory);
	}
	
	public Block createBody() throws LookupException {
		Block adviceBody = new Block();

		/*
		 *	Create the proceed call
		 */
		/*
		 *	Add the proceed-invocation, assign it to a local variable 
		 */
		LocalVariableDeclarator returnVal = new LocalVariableDeclarator(factory().expressionTypeReference());

		/*
		 *	Find the name of the local variable to assign the value to 	
		 */
		String returnVariableName = "_$retval";
		ProgrammingAdvice<?> advice = factory().getAdvice();
		try {
			Returning m = (Returning) advice.modifiers(advice.language(AspectOrientedOOLanguage.class).RETURNING()).get(0);
			if (m.hasParameter()) {
				returnVariableName = m.parameter().getName();
			}
		} catch (ModelException e) {
			throw new ChameleonProgrammerException(e);
		}
				
		VariableDeclaration<LocalVariable> returnValDecl = new VariableDeclaration<LocalVariable>(returnVariableName);
		returnValDecl.setInitialization(factory().getNextExpression());
		returnVal.add(returnValDecl);
	
		adviceBody.addStatement(returnVal);
		
		/*
		 *	Add the advice-body itself 
		 */
		adviceBody.addBlock(advice.body().clone());
		
		/*
		 * 	Add the return statement
		 */
		adviceBody.addStatement(new ReturnStatement(new NamedTargetExpression(returnVariableName)));
		
		return adviceBody;
	}

}
