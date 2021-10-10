package org.aikodi.chameleon.aspect.oo.weave.transform;

import org.aikodi.chameleon.aspect.core.model.pointcut.expression.MatchResult;
import org.aikodi.chameleon.aspect.core.model.pointcut.expression.PointcutExpression;
import org.aikodi.chameleon.aspect.oo.model.advice.weave.transform.runtime.AbstractCoordinator;
import org.aikodi.chameleon.aspect.oo.model.advice.weave.transform.runtime.RuntimeTransformationProvider;
import org.aikodi.chameleon.aspect.oo.model.pointcut.ParameterExposurePointcutExpression;
import org.aikodi.chameleon.aspect.oo.model.pointcut.RuntimePointcutExpression;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.statement.Block;
import org.aikodi.chameleon.oo.statement.Statement;
import org.aikodi.chameleon.oo.variable.FormalParameter;
import org.aikodi.rejuse.predicate.SafePredicate;

import java.util.List;

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
		List<ParameterExposurePointcutExpression> descendants = parameterTree.origin().lexical().descendants(ParameterExposurePointcutExpression.class, predicate);
		if(descendants.isEmpty()) {
			return null;
		} else {
			return descendants.get(0);
		}
	}
	
}
