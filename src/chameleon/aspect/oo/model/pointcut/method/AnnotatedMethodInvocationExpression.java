package chameleon.aspect.oo.model.pointcut.method;

import java.util.List;

import org.rejuse.association.SingleAssociation;

import chameleon.aspect.core.model.pointcut.expression.MatchResult;
import chameleon.aspect.oo.model.pointcut.MethodInvocationPointcutExpression;
import chameleon.core.lookup.LookupException;
import chameleon.core.modifier.Modifier;
import chameleon.oo.expression.MethodInvocation;
import chameleon.oo.method.Method;
import chameleon.oo.modifier.AnnotationModifier;

public class AnnotatedMethodInvocationExpression extends MethodInvocationPointcutExpression {

	private SingleAssociation<AnnotatedMethodInvocationExpression, AnnotationReference> _reference = new SingleAssociation<AnnotatedMethodInvocationExpression, AnnotationReference>(this); 
	
	public AnnotationReference reference() {
		return _reference.getOtherEnd();
	}
	
	public void setReference(AnnotationReference reference) {
		setAsParent(_reference, reference);
	}

	@Override
	public MatchResult match(MethodInvocation joinpoint) throws LookupException {		
		Method target = joinpoint.getElement();
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
	public AnnotatedMethodInvocationExpression clone() {
		AnnotatedMethodInvocationExpression clone = new AnnotatedMethodInvocationExpression();
		clone.setReference(reference().clone());
		return clone;
	}
}