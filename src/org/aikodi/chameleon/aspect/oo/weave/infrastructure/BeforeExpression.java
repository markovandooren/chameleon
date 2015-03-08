package org.aikodi.chameleon.aspect.oo.weave.infrastructure;

import org.aikodi.chameleon.aspect.oo.weave.factory.OOFactory;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.expression.Expression;
import org.aikodi.chameleon.oo.statement.Block;
import org.aikodi.chameleon.util.Util;

public class BeforeExpression extends AdvisedExpressionFactory {

	public BeforeExpression(ExpressionInfrastructureFactory factory) {
		super(factory);
	}
	
	public Block createBody() throws LookupException {
		Block adviceBody = new Block();

		/*
		 *	Create the proceed call
		 */
		/*
		 *	Add the advice-body itself 
		 */
		ExpressionInfrastructureFactory factory = factory();
		adviceBody.addBlock(Util.clone(factory.getAdvice().body()));
		
		/*
		 * 	Add the return statement
		 */
		OOFactory ooFactory = ooFactory();
		Expression nextExpression = factory.getNextExpression();
		adviceBody.addStatement(ooFactory.createReturn(nextExpression));
		
		return adviceBody;
	}


}
