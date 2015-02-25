package org.aikodi.chameleon.aspect.core.model.pointcut.pattern;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.lookup.LookupException;

import be.kuleuven.cs.distrinet.rejuse.predicate.AbstractPredicate;

/**
 * An interface for patterns that are used to match declarations.
 * 
 * @author Marko van Dooren
 */
public abstract class DeclarationPattern extends AbstractPredicate<Declaration, LookupException> {

	@Override
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
	@Override
   public DeclarationPattern negation() {
		return new Not(this);
	}

}
