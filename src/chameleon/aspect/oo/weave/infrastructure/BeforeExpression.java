package chameleon.aspect.oo.weave.infrastructure;

import chameleon.aspect.oo.weave.factory.OOFactory;
import chameleon.core.lookup.LookupException;
import chameleon.oo.expression.Expression;
import chameleon.oo.statement.Block;

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
		adviceBody.addBlock(factory.getAdvice().body().clone());
		
		/*
		 * 	Add the return statement
		 */
		OOFactory ooFactory = ooFactory();
		Expression nextExpression = factory.getNextExpression();
		adviceBody.addStatement(ooFactory.createReturn(nextExpression));
		
		return adviceBody;
	}


}
