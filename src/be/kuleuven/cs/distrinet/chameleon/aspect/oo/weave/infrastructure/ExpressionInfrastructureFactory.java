package be.kuleuven.cs.distrinet.chameleon.aspect.oo.weave.infrastructure;

import be.kuleuven.cs.distrinet.chameleon.aspect.core.weave.infrastructure.AdviceInfrastructureFactory;
import be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.advice.ProgrammingAdvice;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;

public interface ExpressionInfrastructureFactory extends AdviceInfrastructureFactory {
	
	public Expression getNextExpression() throws LookupException;

	public ProgrammingAdvice getAdvice();

	public TypeReference expressionTypeReference() throws LookupException;

}
