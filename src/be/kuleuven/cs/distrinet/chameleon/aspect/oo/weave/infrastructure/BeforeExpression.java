package be.kuleuven.cs.distrinet.chameleon.aspect.oo.weave.infrastructure;

import be.kuleuven.cs.distrinet.chameleon.aspect.oo.weave.factory.OOFactory;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.Block;
import be.kuleuven.cs.distrinet.chameleon.util.Util;

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
