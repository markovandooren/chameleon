package be.kuleuven.cs.distrinet.chameleon.aspect.oo.weave.infrastructure;

import be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.advice.ProgrammingAdvice;
import be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.advice.modifier.Returning;
import be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.language.AspectOrientedOOLanguage;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.chameleon.exception.ModelException;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.ExpressionFactory;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.Block;
import be.kuleuven.cs.distrinet.chameleon.oo.variable.VariableDeclaration;
import be.kuleuven.cs.distrinet.chameleon.support.statement.ReturnStatement;
import be.kuleuven.cs.distrinet.chameleon.support.variable.LocalVariableDeclarator;
import be.kuleuven.cs.distrinet.chameleon.util.Util;

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
		ProgrammingAdvice advice = factory().getAdvice();
		try {
			Returning m = (Returning) advice.modifiers(advice.language(AspectOrientedOOLanguage.class).RETURNING()).get(0);
			if (m.hasParameter()) {
				returnVariableName = m.parameter().name();
			}
		} catch (ModelException e) {
			throw new ChameleonProgrammerException(e);
		}
				
		VariableDeclaration returnValDecl = new VariableDeclaration(returnVariableName);
		returnValDecl.setInitialization(factory().getNextExpression());
		returnVal.add(returnValDecl);
	
		adviceBody.addStatement(returnVal);
		
		/*
		 *	Add the advice-body itself 
		 */
		adviceBody.addBlock(Util.clone(advice.body()));
		
		ExpressionFactory expressionFactory = language().plugin(ExpressionFactory.class);
		/*
		 * 	Add the return statement
		 */
		adviceBody.addStatement(new ReturnStatement(expressionFactory.createNameExpression(returnVariableName)));
		
		return adviceBody;
	}

}
