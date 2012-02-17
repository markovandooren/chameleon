package chameleon.aspect.oo.model.pointcut;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.rejuse.association.SingleAssociation;

import chameleon.aspect.core.model.pointcut.Pointcut;
import chameleon.aspect.core.model.pointcut.PointcutReference;
import chameleon.aspect.core.model.pointcut.expression.AbstractPointcutExpression;
import chameleon.aspect.core.model.pointcut.expression.MatchResult;
import chameleon.aspect.core.model.pointcut.expression.PointcutExpression;
import chameleon.core.declaration.Declaration;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.reference.CrossReference;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.oo.expression.Expression;
import chameleon.oo.expression.NamedTargetExpression;
import chameleon.oo.variable.FormalParameter;
import chameleon.util.Util;
import chameleon.util.concurrent.SafeAction;

public class NamedPointcutExpression extends AbstractPointcutExpression<Element> implements CrossReference<Pointcut> {
	
	public NamedPointcutExpression() {
		
	}
	
	private SingleAssociation<NamedPointcutExpression, PointcutReference> _pointcutReference = new SingleAssociation<NamedPointcutExpression, PointcutReference>(this);
	
	public void setPointcutReference(PointcutReference ref) {
		setAsParent(_pointcutReference, ref);
	}
	
	public PointcutReference pointcutReference() {
		return _pointcutReference.getOtherEnd();
	}

	@Override
	public List<? extends Element> children() {
		return Util.createNonNullList(pointcutReference());
	}


	@Override
	public NamedPointcutExpression clone() {
		NamedPointcutExpression clone = new NamedPointcutExpression();
		
		if (pointcutReference() != null)
			clone.setPointcutReference(pointcutReference().clone());
		
		return clone;
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
		PointcutExpression<?> pointcutExpression = ((PointcutExpression<?>)pointcut.expression()).clone();
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
		pointcutExpression.apply(ParameterExposurePointcutExpression.class, new SafeAction<ParameterExposurePointcutExpression>() {
			@Override
			protected void actuallyPerform(ParameterExposurePointcutExpression t) throws Exception {
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
	public LookupStrategy targetContext() throws LookupException {
		return getElement().targetContext();
	}
	
}