package org.aikodi.chameleon.oo.type.generics;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeFixer;
import org.aikodi.chameleon.oo.type.TypeReference;

import java.util.HashSet;
import java.util.Set;

/**
 * A class of type arguments.
 * 
 * @author Marko van Dooren
 */
public abstract class TypeArgument extends ElementImpl implements ElementWithTypeBounds {

	public abstract Type type() throws LookupException;

	/**
	 * Check whether the set of types represented by this type argument
	 * provably contains the set of types represented by the other
	 * type argument
	 * 
	 * @param other The other type argument. The other type argument cannot
	 * be null.
	 *  
	 * @return True if and only if every type that is a valid type for the given 
	 * type argument is a valid type for this type argument.
	 * 
	 * @throws LookupException
	 */
	public boolean contains(TypeArgument other, TypeFixer trace) throws LookupException	{
	  return type().contains(other.type(), trace);
	}

	/**
	 * @return The upper bound of the set of types that are valid types
	 * for this type argument.
	 * 
	 * @throws LookupException
	 */
	public abstract Type upperBound() throws LookupException;

	/**
	 * @return The lower bound of the set of types that are valid types
	 * for this type argument.
	 * 
	 * @throws LookupException
	 */
	public abstract Type lowerBound() throws LookupException;

	/**
	 * Capture this type argument for the given type parameter.
	 * 
	 * @param formal The parameter for which this type argument is used.
	 *               The type parameter cannot be null.
	 * @return
	 */
	public abstract TypeParameter capture(FormalTypeParameter formal);

//	public abstract List<TypeConstraint> capture(List<TypeConstraint> constraints);

	/**
	 * Return the type reference that must be used for substitution of a formal parameter.
	 * 
	 * @return
	 */
	public TypeReference substitutionReference() {
		throw new ChameleonProgrammerException();
	}

	@Override
	public boolean uniSameAs(Element other) throws LookupException {
		return (other.getClass().equals(getClass())) && (type().sameAs(((TypeArgument)other).type()));
	}

	@Override
	public int hashCode() {
		try {
			int hashCode = type().hashCode();
			return hashCode;
		} catch (LookupException e) {
			throw new ChameleonProgrammerException();
		}
	}

	protected TypeConstraint cloneAndResetTypeReference(TypeConstraint constraint, Element lookupParent) {
		TypeConstraint kloon = clone(constraint);
		ObjectOrientedLanguage language = language(ObjectOrientedLanguage.class);
		TypeReference nl = language.createNonLocalTypeReference(kloon.typeReference(), lookupParent);
		kloon.setTypeReference(nl);
		return kloon;
	}

	public boolean sameAs(TypeArgument argument, TypeFixer trace) throws LookupException {
		TypeFixer newTrace = trace.clone();
		return uniSameAs(argument,newTrace) || argument.uniSameAs(this,newTrace);
	}

	public boolean uniSameAs(TypeArgument argument, TypeFixer trace) throws LookupException {
		//FIXME the getClass() comparison is iffy
		return (argument.getClass().equals(getClass())) && (type().sameAs(argument.type(),trace));
	}

//	/**
//	 * @param other
//	 * @param trace
//	 * @return
//	 * @throws LookupException 
//	 */
//	public final boolean contains(TypeParameter other, TypeFixer trace) throws LookupException {
//		boolean result;
//		result = contains(other.argument(),trace);
//		return result;
//	}

	/**
	 * @param visited
	 * @return
	 */
	public abstract String toString(Set<Element> visited);

	public final String toString() {
		return toString(new HashSet<>());
	}

	/**
	 * @return
	 * @throws LookupException 
	 */
	public abstract boolean isWildCardBound() throws LookupException;
}
