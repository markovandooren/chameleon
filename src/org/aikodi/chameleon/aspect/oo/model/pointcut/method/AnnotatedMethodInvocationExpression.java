package org.aikodi.chameleon.aspect.oo.model.pointcut.method;

import java.util.List;

import org.aikodi.chameleon.aspect.core.model.pointcut.expression.MatchResult;
import org.aikodi.chameleon.aspect.oo.model.pointcut.MethodInvocationPointcutExpression;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.modifier.Modifier;
import org.aikodi.chameleon.oo.expression.MethodInvocation;
import org.aikodi.chameleon.oo.method.Method;
import org.aikodi.chameleon.oo.modifier.AnnotationModifier;
import org.aikodi.chameleon.util.association.Single;

public class AnnotatedMethodInvocationExpression extends MethodInvocationPointcutExpression {

	private Single<AnnotationReference> _reference = new Single<AnnotationReference>(this); 
	
	public AnnotationReference reference() {
		return _reference.getOtherEnd();
	}
	
	public void setReference(AnnotationReference reference) {
		set(_reference, reference);
	}

	@Override
	public MatchResult match(MethodInvocation joinpoint) throws LookupException {		
		Method target = (Method) joinpoint.getElement();
		List<Modifier> modifiers = target.modifiers();
		
		for (Modifier modifier : modifiers) {
			if (modifier instanceof AnnotationModifier) {
				if (((AnnotationModifier) modifier).typeReference().getElement().name().equals(reference().referencendName()))
					return new MatchResult<MethodInvocation>(this, joinpoint);
			}
		}
		
		return MatchResult.noMatch();
	}

	@Override
	public AnnotatedMethodInvocationExpression cloneSelf() {
		return new AnnotatedMethodInvocationExpression();
	}
}
