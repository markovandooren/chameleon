package be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.pointcut.method;

import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.expression.MatchResult;
import be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.pointcut.MethodInvocationPointcutExpression;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.MethodInvocation;
import be.kuleuven.cs.distrinet.chameleon.oo.method.Method;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

public class SignatureMethodInvocationPointcutExpression extends MethodInvocationPointcutExpression {
	private Single<MethodReference> _methodReference = new Single<MethodReference>(this);
	
	public SignatureMethodInvocationPointcutExpression(MethodReference methodReference) {
		setMethodReference(methodReference);
	}

	private void setMethodReference(MethodReference methodReference) {
		set(_methodReference, methodReference);
	}
	
	private MethodReference methodReference() {
		return _methodReference.getOtherEnd();
	}

	@Override
	public MatchResult match(MethodInvocation joinpoint) throws LookupException {		
		// This might not be foolproof in all cases (inner classes etc), need further testing - TODO
		Type definedType = (Type) joinpoint.getElement().nearestAncestor(Type.class);
		
		if (methodReference().matches((Method)joinpoint.getElement(), definedType)) {
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
	protected SignatureMethodInvocationPointcutExpression cloneSelf() {
		return new SignatureMethodInvocationPointcutExpression(null); 
	}

}
