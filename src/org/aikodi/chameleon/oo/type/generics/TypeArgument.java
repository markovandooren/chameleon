package org.aikodi.chameleon.oo.type.generics;



import java.util.ArrayList;
import java.util.List;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeReference;
import org.aikodi.chameleon.util.Pair;

public abstract class ActualTypeArgument extends ElementImpl {

	public ActualTypeArgument() {
	}
	
//	public abstract ActualTypeArgument clone();
	
	public abstract Type type() throws LookupException;
	
	public final boolean contains(ActualTypeArgument other) throws LookupException {
		return other.upperBound().subTypeOf(upperBound()) && lowerBound().subTypeOf(other.lowerBound());
	}
	
	public abstract Type upperBound() throws LookupException;
	
	public abstract Type lowerBound() throws LookupException;

	public abstract TypeParameter capture(FormalTypeParameter formal, List<TypeConstraint> accumulator);
	
	/**
	 * Return the type reference that must be used for substitution of a formal parameter.
	 * 
	 * @param parameter
	 * @return
	 */
	public TypeReference substitutionReference() {
		throw new ChameleonProgrammerException();
	}

	@Override
	public boolean uniSameAs(Element other) throws LookupException {
		return (other.getClass().equals(getClass())) && (type().sameAs(((ActualTypeArgument)other).type()));
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

	public boolean sameAs(ActualTypeArgument argument, List<Pair<TypeParameter, TypeParameter>> trace) throws LookupException {
		List<Pair<TypeParameter, TypeParameter>> newTrace = new ArrayList<Pair<TypeParameter, TypeParameter>>(trace);
		return uniSameAs(argument,newTrace) || argument.uniSameAs(this,newTrace);
	}

	public boolean uniSameAs(ActualTypeArgument argument, List<Pair<TypeParameter, TypeParameter>> trace) throws LookupException {
		//FIXME the getClass() comparison is iffy
		return (argument.getClass().equals(getClass())) && (type().sameAs(argument.type(),trace));
	}

}
