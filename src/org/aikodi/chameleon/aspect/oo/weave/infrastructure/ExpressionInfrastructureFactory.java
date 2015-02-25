package org.aikodi.chameleon.aspect.oo.weave.infrastructure;

import org.aikodi.chameleon.aspect.core.weave.infrastructure.AdviceInfrastructureFactory;
import org.aikodi.chameleon.aspect.oo.model.advice.ProgrammingAdvice;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.expression.Expression;
import org.aikodi.chameleon.oo.type.TypeReference;

public interface ExpressionInfrastructureFactory extends AdviceInfrastructureFactory {
	
	public Expression getNextExpression() throws LookupException;

	public ProgrammingAdvice getAdvice();

	public TypeReference expressionTypeReference() throws LookupException;

}
