package chameleon.aspect.oo.weave.infrastructure;

import chameleon.core.lookup.LookupException;
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
		adviceBody.addBlock(factory().getAdvice().body().clone());
		
		/*
		 * 	Add the return statement
		 */
		adviceBody.addStatement(ooFactory().createReturn(factory().getNextExpression()));
		
		return adviceBody;
	}


}