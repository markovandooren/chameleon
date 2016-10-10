package org.aikodi.chameleon.core.declaration;

import java.util.Collection;
import java.util.Set;

import org.aikodi.chameleon.core.lookup.LookupException;

public abstract class DeclarationRelation {

//	public default void next(Declaration declaration, DeclarationContainerRelation containerRelation, Set<Declaration> accumulator) throws LookupException {
//		containerRelation.next(declaration, this, accumulator);
//	}

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
	 * @param superDeclaration A declaration from a container higer in the hierarchy.
	 *                       The declaration cannot be null.
	 * @return True if and only if the super declaration matches the sub declaration.
	 * 
	 * @throws LookupException
	 */
	public abstract boolean matches(Declaration subDeclaration, Declaration superDeclaration) throws LookupException;
	
	/**
	 * 
	 * @param newValues
	 * @param local
	 * @param accumulator
	 * @return
	 * @throws LookupException
	 */
	public boolean accumulate(Collection<? extends Declaration> newValues, Declaration local, Set<Declaration> accumulator)  throws LookupException {
		boolean cont = true;
		for(Declaration declaration: newValues) {
			if(cont && matches(local, declaration)) {
				accumulator.add(declaration);
				cont = cont && multiple;
			}
		}
		return cont;
	}

	
//	public default boolean matches(Declaration local, Declaration declaration) throws LookupException {
//		return declaration.compatibleSignature(local);
//	}
}
