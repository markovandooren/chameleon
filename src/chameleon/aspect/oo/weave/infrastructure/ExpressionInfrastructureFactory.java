package chameleon.aspect.oo.weave.infrastructure;

import chameleon.aspect.core.weave.infrastructure.AdviceInfrastructureFactory;
import chameleon.aspect.oo.model.advice.ProgrammingAdvice;
import chameleon.core.lookup.LookupException;
import chameleon.oo.expression.Expression;
import chameleon.oo.type.TypeReference;

public interface ExpressionInfrastructureFactory extends AdviceInfrastructureFactory {
	
	public Expression<?> getNextExpression() throws LookupException;

	public ProgrammingAdvice<?> getAdvice();

	public TypeReference expressionTypeReference() throws LookupException;

}
