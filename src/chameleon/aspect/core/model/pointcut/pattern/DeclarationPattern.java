package chameleon.aspect.core.model.pointcut.pattern;

import org.rejuse.predicate.UnsafePredicate;

import chameleon.aspect.core.model.pointcut.expression.AbstractPointcutExpression;
import chameleon.core.declaration.Declaration;
import chameleon.core.lookup.LookupException;

/**
 * An interface for patterns that are used to match declarations.
 * 
 * @author Marko van Dooren
 */
public abstract class DeclarationPattern extends UnsafePredicate<Declaration<?,?>, LookupException> {

	public abstract DeclarationPattern clone();
	
	/**
	 * Return the conjunction of this declaration pattern and the given declaration pattern.
	 * @param other The declaration pattern with which a conjunction must be formed.
	 */
 /*@
   @ public behavior
   @
   @ pre other != null;
   @
   @ post (\forall Declaration d; ; \result.eval(d) == eval(d) && other.eval(d));  
   @*/
	public DeclarationPattern and(final DeclarationPattern other) {
		return new And(this, other);
	}

	/**
	 * Return the disjunction of this declaration pattern and the given declaration pattern.
	 * @param other The declaration pattern with which a disjunction must be formed.
	 */
 /*@
   @ public behavior
   @
   @ pre other != null;
   @
   @ post (\forall Declaration d; ; \result.eval(d) == eval(d) || other.eval(d));  
   @*/
	public DeclarationPattern or(final DeclarationPattern other) {
		return new Or(this, other);
	}
	
	/**
	 * Return the negation of this declaration pattern.
	 */
 /*@
   @ public behavior
   @
   @ pre other != null;
   @
   @ post (\forall Declaration d; ; \result.eval(d) == ! eval(d));  
   @*/
	public DeclarationPattern negation() {
		return new Not(this);
	}

}
