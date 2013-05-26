package be.kuleuven.cs.distrinet.chameleon.oo.type.generics;



import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.element.ElementImpl;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.chameleon.oo.language.ObjectOrientedLanguage;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;
import be.kuleuven.cs.distrinet.chameleon.util.Pair;

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
		ObjectOrientedLanguage language = language(ObjectOrientedLanguage.class);
		TypeConstraint kloon = clone(constraint);
		TypeReference nl = language.createNonLocalTypeReference(kloon.typeReference(), lookupParent);
		kloon.setTypeReference(nl);
		return kloon;
	}

	public boolean sameAs(ActualTypeArgument argument, List<Pair<TypeParameter, TypeParameter>> trace) throws LookupException {
		List<Pair<TypeParameter, TypeParameter>> newTrace = new ArrayList<Pair<TypeParameter, TypeParameter>>(trace);
		return uniSameAs(argument,newTrace) || argument.uniSameAs(this,newTrace);
	}

	public boolean uniSameAs(ActualTypeArgument argument, List<Pair<TypeParameter, TypeParameter>> trace) throws LookupException {
		return (argument.getClass().equals(getClass())) && (type().sameAs(((ActualTypeArgument)argument).type(),trace));
	}

}
