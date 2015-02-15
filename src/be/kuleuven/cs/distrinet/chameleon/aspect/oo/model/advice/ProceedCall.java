package be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.advice;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.advice.Advice;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.validation.BasicProblem;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.util.association.Multi;

public class ProceedCall extends Expression {

	public ProceedCall() {

	}

	@Override
	public ProceedCall cloneSelf() {
		return new ProceedCall();
	}

	@Override
	protected Type actualType() throws LookupException {
		ProgrammingAdvice parentAdvice = nearestAncestor(ProgrammingAdvice.class);
			
		if (parentAdvice != null)
			return parentAdvice.actualReturnType();
		
		return null;
	}

	@Override
	public Verification verifySelf() {
		Verification result = Valid.create();
		
		Advice advice = nearestAncestor(Advice.class);
		
		if (advice == null) {
			result = result.and(new BasicProblem(this, "Proceed calls are only allowed in advice bodies."));
		} 
		// FIXME: optional validation, should check what is proceeded and how many parameters it needs
//		else {
//			Iterator<FormalParameter> adviceIterator = advice.formalParameters().iterator();
//			Iterator<Expression> proceedIterator = getActualParameters().iterator();
//			
//			while (adviceIterator.hasNext() && proceedIterator.hasNext()) {
//				FormalParameter param = adviceIterator.next();
//				Expression expr = proceedIterator.next();
//				
//				try {
//					if (!(expr.getType().sameAs(param.getType()) || expr.getType().subTypeOf(param.getType())))
//						result = result.and(new BasicProblem(this, "Incompatible types: given " + expr.getType().getName() + ", expected " + param.getType().getName()));
//				} catch (LookupException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			
//			if (adviceIterator.hasNext() || proceedIterator.hasNext())
//				result = result.and(new BasicProblem(this, "Expecting " + advice.formalParameters().size() + " parameter(s), found " + getActualParameters().size()));
//		}
		
		return result;
	}

	private Multi<Expression> _parameters = new Multi<Expression>(this);

	public void addArgument(Expression parameter) {
		add(_parameters, parameter);
	}

	public void addAllArguments(List<Expression> parameters) {
		for (Expression parameter : parameters) {
			addArgument(parameter);
		}
	}

	public void removeArgument(Expression parameter) {
		remove(_parameters,parameter);
	}

	public List<Expression> getActualParameters() {
		return _parameters.getOtherEnds();
	}
}
