package chameleon.aspect.oo.weave.transform;

import java.util.List;

import org.rejuse.predicate.SafePredicate;

import chameleon.aspect.core.model.pointcut.expression.MatchResult;
import chameleon.aspect.core.model.pointcut.expression.PointcutExpression;
import chameleon.aspect.oo.model.advice.weave.transform.runtime.AbstractCoordinator;
import chameleon.aspect.oo.model.advice.weave.transform.runtime.RuntimeTransformationProvider;
import chameleon.aspect.oo.model.pointcut.ParameterExposurePointcutExpression;
import chameleon.aspect.oo.model.pointcut.RuntimePointcutExpression;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.oo.statement.Block;
import chameleon.oo.statement.Statement;
import chameleon.oo.variable.FormalParameter;

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