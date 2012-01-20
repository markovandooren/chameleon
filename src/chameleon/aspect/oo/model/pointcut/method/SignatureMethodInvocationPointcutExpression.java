package chameleon.aspect.oo.model.pointcut.method;

import java.util.ArrayList;
import java.util.List;

import org.rejuse.association.SingleAssociation;

import chameleon.aspect.core.model.pointcut.expression.MatchResult;
import chameleon.aspect.oo.model.pointcut.MethodInvocationPointcutExpression;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.oo.expression.MethodInvocation;
import chameleon.oo.type.Type;
import chameleon.util.Util;

public class SignatureMethodInvocationPointcutExpression<E extends SignatureMethodInvocationPointcutExpression<E>> extends MethodInvocationPointcutExpression<E> {
	private SingleAssociation<SignatureMethodInvocationPointcutExpression, MethodReference> _methodReference = new SingleAssociation<SignatureMethodInvocationPointcutExpression, MethodReference>(this);
	
	public SignatureMethodInvocationPointcutExpression(MethodReference methodReference) {
		setMethodReference(methodReference);
	}

	private void setMethodReference(MethodReference methodReference) {
		setAsParent(_methodReference, methodReference);
	}
	
	private MethodReference methodReference() {
		return _methodReference.getOtherEnd();
	}

	@Override
	public MatchResult match(MethodInvocation joinpoint) throws LookupException {		
		// This might not be foolproof in all cases (inner classes etc), need further testing - TODO
		Type definedType = (Type) joinpoint.getElement().nearestAncestor(Type.class);
		
		if (methodReference().matches(joinpoint.getElement(), definedType)) {
			return new MatchResult<MethodInvocation>(this, (MethodInvocation) joinpoint);
		} else {
			return MatchResult.noMatch();
		}
	}


	/**
	 * 	Match rules:
	 * 			hrm.Person matches with:
	 * 				- hrm.Person (or any wildcard combo, e.g. h*m.P*)
	 * 				- **.Person  (or any wildcard combo, e.g. **.P*)
	 * 				- hrm.**     (or any wildcard combo, e.g. h*m.**)
	 * 				- **.**
	 * 				- **
	 * 
	 * @param jpFqn_
	 * @param definedFqn_
	 * @return
	 */
	private boolean sameFQNWithWildcard(String jpFqn_, String definedFqn_) {
		String[] jpFqn = jpFqn_.split("\\.");
		String[] definedFqn = definedFqn_.split("\\.");
		
		// Special case: if the FQN of the call is a complete wildcard, match everything
		if (definedFqn.length == 1 && definedFqn[0].equals("**"))
			return true;
		
		if (jpFqn.length != definedFqn.length)
			return false;
		
		for (int i = 0; i < jpFqn.length; i++)
			if (!sameAsWithWildcard(jpFqn[i], definedFqn[i]))
				return false;
		
		return true;
	}

	/**
	 * 	Check if s1 is the same as s2 - s2 can contain a wild card (** = any character 0 or more times), s1 can
	 *  contain the wild card character but it will not be treated as such.
	 *  
	 */
	public boolean sameAsWithWildcard(String s1, String s2) {
		// Turn s2 into a regexp. We convert the wild card character (**) to a regexp-wildcard (.*) and treat
		// all the rest as a literal (between \Q and \E)
		String regexp = "\\Q" + s2.replace("**", "\\E(.*)\\Q") + "\\E"; 
		
		return s1.matches(regexp);
	}

	@Override
	public List<? extends Element> children() {
		List<Element> result = new ArrayList<Element>();
		
		Util.addNonNull(methodReference(), result);
		
		return result;
	}

	@Override
	public E clone() {
		return (E) new SignatureMethodInvocationPointcutExpression<E>(methodReference().clone()); 
	}

}