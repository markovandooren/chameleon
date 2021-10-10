package org.aikodi.chameleon.aspect.oo.model.pointcut;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.expression.NameExpression;
import org.aikodi.chameleon.oo.type.BasicTypeReference;
import org.aikodi.chameleon.oo.type.TypeReference;
import org.aikodi.chameleon.util.association.Single;

import java.util.Collections;
import java.util.List;

public abstract class TypePointcutExpression extends AbstractParameterExposurePointcutExpression implements RuntimePointcutExpression<Element> {
	public TypePointcutExpression(NameExpression parameter) {
		setParameter(parameter);
	}
	
	private Single<NameExpression> _parameter = new Single<NameExpression>(this, "parameter");
	
	public NameExpression parameter() {
		return _parameter.getOtherEnd();
	}
	
	public void setParameter(NameExpression parameter) {
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
	public List<NameExpression> parameters() {
		return Collections.singletonList(parameter());
	}
}
