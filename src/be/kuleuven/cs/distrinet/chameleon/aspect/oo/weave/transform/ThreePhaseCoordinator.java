package be.kuleuven.cs.distrinet.chameleon.aspect.oo.weave.transform;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.expression.MatchResult;
import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.expression.PointcutExpression;
import be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.advice.weave.transform.runtime.AbstractCoordinator;
import be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.advice.weave.transform.runtime.RuntimeTransformationProvider;
import be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.pointcut.ParameterExposurePointcutExpression;
import be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.pointcut.RuntimePointcutExpression;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.Block;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.Statement;
import be.kuleuven.cs.distrinet.chameleon.oo.variable.FormalParameter;
import be.kuleuven.cs.distrinet.rejuse.predicate.SafePredicate;

public abstract class ThreePhaseCoordinator<T extends Element> extends AbstractCoordinator<T> {

	public ThreePhaseCoordinator(RuntimeTransformationProvider adviceTransformationProvider, MatchResult<? extends Element> matchResult) {
		super(adviceTransformationProvider, matchResult);
	}
	
	protected RuntimePointcutExpression<?> getRuntimeTree(PointcutExpression<?> initialTree) {
		// Part one: get all the runtime pointcut expressions but maintain the structure (and/or/...)
		SafePredicate<PointcutExpression<?>> runtimeFilter = new SafePredicate<PointcutExpression<?>>() {
			@Override
			public boolean eval(PointcutExpression<?> object) {
				return (object instanceof RuntimePointcutExpression) &&
				       getAdviceTransformationProvider().supports(object);
			}
		};
		// Cast is safe due to the filter
		return (RuntimePointcutExpression<?>) initialTree.retainOnly(runtimeFilter);
	}

	protected Block getSecondPhase(PointcutExpression<?> initialTree, List<FormalParameter> parameters) throws LookupException {
		Block secondPhase = new Block();
		
		SafePredicate<PointcutExpression<?>> parameterInjectionFilter = new SafePredicate<PointcutExpression<?>>() {

			@Override
			public boolean eval(PointcutExpression<?> object) {
				return (object instanceof ParameterExposurePointcutExpression) &&
						   getAdviceTransformationProvider().supports(object);
			}
		};
		
		// Cast is safe due to the filter
		PointcutExpression<?> parameterTree = initialTree.retainOnly(parameterInjectionFilter);
		for (FormalParameter fp : parameters) {
			ParameterExposurePointcutExpression<?> exposingParameter = searchParameterExpression(parameterTree, fp);
			List<Statement> parameterInjector = getAdviceTransformationProvider().getRuntimeParameterInjectionProvider(exposingParameter).getParameterExposureDeclaration((ParameterExposurePointcutExpression<?>) exposingParameter.origin(), fp);
			secondPhase.addStatements(parameterInjector);
		}
		
		return secondPhase;
	}

	private ParameterExposurePointcutExpression<?> searchParameterExpression(PointcutExpression<?> parameterTree, final FormalParameter fp) {
		SafePredicate<ParameterExposurePointcutExpression> predicate = new SafePredicate<ParameterExposurePointcutExpression>() {
			@Override
			public boolean eval(ParameterExposurePointcutExpression object) {
				return object.hasParameter(fp);
			}
		};
		if(parameterTree instanceof ParameterExposurePointcutExpression  &&  predicate.eval((ParameterExposurePointcutExpression) parameterTree)) {
			return (ParameterExposurePointcutExpression<?>) parameterTree;
		}
		List<ParameterExposurePointcutExpression> descendants = parameterTree.origin().descendants(ParameterExposurePointcutExpression.class, predicate);
		if(descendants.isEmpty()) {
			return null;
		} else {
			return (ParameterExposurePointcutExpression<?>) descendants.get(0);
		}
	}
	
}
