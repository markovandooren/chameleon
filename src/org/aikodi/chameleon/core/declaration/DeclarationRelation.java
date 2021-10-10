package org.aikodi.chameleon.core.declaration;

import org.aikodi.chameleon.core.lookup.LookupException;

import java.util.Collection;
import java.util.Set;

/**
 * A class of relations between declarations.
 * 
 * Examples are overriding, aliasing, and hiding.
 * 
 * @author Marko van Dooren
 */
public abstract class DeclarationRelation {

	/**
	 * True if a relation can exists between a declaration in a sub container
	 * and multiple declarations in a super container.
	 */
	private boolean multiple = true;
	
	/**
	 * Create a new declaration relation.
	 * 
	 * @param multiple Indicates whether this declaration relation allows a sub declaration
	 *                 to have a relation with multiple declarations <b>in the same super container</b>.
	 *                 For overriding in Java, e.g. this is false because Java uses syntactic overloading.
	 *                 For aliasing, or for languages that use dynamic binding on argument types, 
	 *                 multiple declaration within a single container can match. 
	 *                 
	 */
	public DeclarationRelation(boolean multiple) {
		this.multiple = multiple;
	}

	/**
	 * Check whether the super declaration matches the sub declaration.
	 * 
	 * @param subDeclaration The declaration from a container lower in the hierarchy.
	 *                       The declaration cannot be null.
	 * @param superDeclaration A declaration from a container higher in the hierarchy.
	 *                       The declaration cannot be null.
	 * @return True if and only if the super declaration matches the sub declaration.
	 * 
	 * @throws LookupException
	 */
	public abstract boolean contains(Declaration subDeclaration, Declaration superDeclaration) throws LookupException;
	
	/**
	 * Accumulate the new declarations that are in a relation with the given sub declaration.
	 * 
	 * @param newDeclarations A collection containing the new declarations to
	 *                        be investigated. The collection cannot be null.
	 * @param subDeclaration The sub declaration. The sub declaration cannot be null.
	 * @param accumulator The set to which the declarations are added with which
	 *                    the sub declaration has a relation.
	 * @return True if the accumulation process needs to continue. False otherwise.
	 *         The accumulation process needs to continue if multiple matches
	 *         are possible, or if no match was found in the set of new declarations.
	 *         
	 * @throws LookupException A lookup exception occurred while checking if any
	 *                         of the given new declarations are related to the
	 *                         given sub declaration according to this relation.
	 */
	public boolean accumulate(Collection<? extends Declaration> newDeclarations, Declaration subDeclaration, Set<Declaration> accumulator)  throws LookupException {
		boolean continueAccumulation = true;
		for(Declaration declaration: newDeclarations) {
			if(continueAccumulation && contains(subDeclaration, declaration)) {
				accumulator.add(declaration);
				continueAccumulation = continueAccumulation && multiple;
			}
		}
		return continueAccumulation;
	}

	
}
