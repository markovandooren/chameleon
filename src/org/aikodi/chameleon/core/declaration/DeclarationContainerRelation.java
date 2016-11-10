package org.aikodi.chameleon.core.declaration;

import java.util.List;
import java.util.Set;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;

import be.kuleuven.cs.distrinet.rejuse.function.BiFunction;

/**
 * A relation between declaration containers.
 * 
 * @author Marko van Dooren
 */
public interface DeclarationContainerRelation extends Element {
	//  	public boolean overrides(Declaration overriding, Declaration overridden) throws LookupException;

	/**
	 * Return the target of the relation. This is the declaration container
	 * that the container of this relation has a relation with.
	 * 
	 * @throws LookupException
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public DeclarationContainer target() throws LookupException;
	
//	public Declaration follow(Declaration declaration, DeclarationContainer to) throws LookupException;

	/**
	 * Add the directly overridden of the given declaration in the lookup hierarchy to the given set.
	 * This can for example be used to find overridden or hidden declarations.
	 * 
	 * By default, the call is redirected to the {@link #target()} of this relation.
	 * 
	 * @param declaration The declaration for which the next occurrences must be added.
	 *                    The declaration cannot be null.
	 * @param matchCondition The condition that determines when two declarations match
	 *                    The match condition cannot be null.
	 * @param accumulator The set in which all matching declarations are added.
	 *                    The set cannot be null.
	 * @throws LookupException
	 */
 /*@
   @ public behavior
   @
   @ pre declaration != null;
   @ pre matchCondition != null;
   @ pre accumulator != null;
   @*/
	public default void directlyOverridden(Declaration declaration,	DeclarationRelation relation, Set<Declaration> accumulator) throws LookupException {
		List<? extends Declaration> locals = target().locallyDeclaredDeclarations();
    boolean cont = relation.accumulate(locals, declaration, accumulator);
    if(cont) {
		  target().directlyOverriddenDeclarations(declaration, relation, accumulator);
    }
	}

	public default void directlyAliasedDeclarations(Declaration declaration, Set<Declaration> accumulator) throws LookupException {
		target().directlyAliasedDeclarations(declaration, accumulator);
	}

	public default void directlyAliasingDeclarations(Declaration declaration, Set<Declaration> accumulator) throws LookupException {
		target().directlyAliasingDeclarations(declaration, accumulator);
	}
}