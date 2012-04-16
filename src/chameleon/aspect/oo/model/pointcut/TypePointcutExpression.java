package chameleon.aspect.oo.model.pointcut;

import java.util.Collections;
import java.util.List;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.oo.expression.NamedTargetExpression;
import chameleon.oo.type.BasicTypeReference;
import chameleon.oo.type.TypeReference;
import chameleon.util.association.Single;

public abstract class TypePointcutExpression extends AbstractParameterExposurePointcutExpression implements RuntimePointcutExpression<Element> {
	public TypePointcutExpression(NamedTargetExpression parameter) {
		setParameter(parameter);
	}
	
	public abstract TypePointcutExpression clone();

	private Single<NamedTargetExpression> _parameter = new Single<NamedTargetExpression>(this);
	
	public NamedTargetExpression parameter() {
		return _parameter.getOtherEnd();
	}
	
	public void setParameter(NamedTargetExpression parameter) {
		set(_parameter, parameter);
	}
	
	public TypeReference getType() {
		try {
			return new BasicTypeReference(parameter().getType().getFullyQualifiedName());
		} catch (LookupException e) {
			
		}
		
		return null;
	}

	
	@Override
	public List<NamedTargetExpression> parameters() {
		return Collections.singletonList(parameter());
	}
}