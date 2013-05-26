package be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.pointcut;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.Pointcut;
import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.PointcutReference;
import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.expression.AbstractPointcutExpression;
import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.expression.MatchResult;
import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.expression.PointcutExpression;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.reference.CrossReference;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.NamedTargetExpression;
import be.kuleuven.cs.distrinet.chameleon.oo.variable.FormalParameter;
import be.kuleuven.cs.distrinet.chameleon.util.action.SafeAction;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

public class NamedPointcutExpression extends AbstractPointcutExpression<Element> implements CrossReference<Pointcut> {
	
	public NamedPointcutExpression() {
		
	}
	
	private Single<PointcutReference> _pointcutReference = new Single<PointcutReference>(this);
	
	public void setPointcutReference(PointcutReference ref) {
		set(_pointcutReference, ref);
	}
	
	public PointcutReference pointcutReference() {
		return _pointcutReference.getOtherEnd();
	}

	@Override
	protected NamedPointcutExpression cloneSelf() {
		return new NamedPointcutExpression();
	}

	@Override
	public ProgrammingPointcut getElement() throws LookupException {
		if (pointcutReference() == null)
			return null;
		
		return (ProgrammingPointcut) pointcutReference().getElement();
	}

	@Override
	public Declaration getDeclarator() throws LookupException {
		if (pointcutReference() == null)
			return null;
		
		return pointcutReference().getDeclarator();
	}

	@Override
	public MatchResult match(Element joinpoint) throws LookupException {
		/* We replace this reference to a pointcut, by the pointcut expression of the pointcut. However, we must take into account that
		 parameters have to be renamed.
		 	e.g.
		 	
		 		Pointcut a(Foo param) : thisType(param);
		 		Pointcut b(Foo realParam): call(void *.doSomething()) && a(realParam)
		
		
				=> Pointcut b(Foo realParam): call(void *.doSomething()) && thisType(realParam)
		
		*/
		
//		PointcutExpression<?> pointcutExpression = ((PointcutExpression<?>) getElement().expression()).expand();
		Pointcut pointcut = getElement();
		PointcutExpression<?> pointcutExpression = clone((PointcutExpression<?>)pointcut.expression());
		pointcutExpression.setUniParent(pointcut);
		
		if (!pointcutReference().getActualParameters().isEmpty()) {
			// Map is from->to
			Map<String, String> parameterNamesMap = new HashMap<String, String>();
			
			Iterator<FormalParameter> pointcutParameters = getElement().parameters().iterator();
			Iterator<Expression> referenceParameters = pointcutReference().getActualParameters().iterator();
			
			while (pointcutParameters.hasNext() && referenceParameters.hasNext()) {
				Expression _nextReferenceParam  = referenceParameters.next();
				
				if (!(_nextReferenceParam instanceof NamedTargetExpression))
					throw new ChameleonProgrammerException("Pointcut reference has a parameter that isn't a named target expr");
				
				NamedTargetExpression nextReferenceParam = (NamedTargetExpression) _nextReferenceParam;
				FormalParameter nextPointcutParam = pointcutParameters.next();
				
				parameterNamesMap.put(nextPointcutParam.getName(), nextReferenceParam.name());
			}
			
			// We know there are parameters, so we know the pointcut expression must be a ParameterExposurePointcutExpression, so the cast is no problem
			renameParameters(pointcutExpression, parameterNamesMap);
		}
		
		return pointcutExpression.matches(joinpoint);
					
//		//TODO Implement the functionality from expand() here, and remove expand, which isn't necessary at all.
//		throw new ChameleonProgrammerException("Named pointcut expression not expanded before operations!");
	}

	private void renameParameters(PointcutExpression<?> pointcutExpression, final Map<String, String> parameterNamesMap) {
		 //((ParameterExposurePointcutExpression<?,?>) pointcutExpression).renameParameters(parameterNamesMap);
		pointcutExpression.apply(new SafeAction<ParameterExposurePointcutExpression>(ParameterExposurePointcutExpression.class) {
			@Override
			public void perform(ParameterExposurePointcutExpression t) {
				t.renameParameters(parameterNamesMap);
			}
		});
	}
	
	/**
	 * 	{@inheritDoc}
	 */
	public boolean hasParameter(FormalParameter fp) {
		return pointcutReference().hasParameter(fp);
	}

	@Override
	public Class<? extends Element> joinPointType() throws LookupException {
		return getElement().expression().joinPointType();
	}

	@Override
	public LookupContext targetContext() throws LookupException {
		return getElement().targetContext();
	}
	
}
