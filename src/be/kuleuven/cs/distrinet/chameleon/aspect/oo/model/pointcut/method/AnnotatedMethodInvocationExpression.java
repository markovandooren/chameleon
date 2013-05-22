package be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.pointcut.method;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.expression.MatchResult;
import be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.pointcut.MethodInvocationPointcutExpression;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.modifier.Modifier;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.MethodInvocation;
import be.kuleuven.cs.distrinet.chameleon.oo.method.Method;
import be.kuleuven.cs.distrinet.chameleon.oo.modifier.AnnotationModifier;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

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
				if (((AnnotationModifier) modifier).name().equals(reference().referencendName()))
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
