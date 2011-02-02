package chameleon.oo.type.generics;



import java.util.ArrayList;
import java.util.List;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespace.NamespaceElementImpl;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.oo.language.ObjectOrientedLanguage;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeReference;
import chameleon.util.Pair;

public abstract class ActualTypeArgument<E extends ActualTypeArgument> extends NamespaceElementImpl<E> 
//implements TypeReference<E> 
{

	public ActualTypeArgument() {
	}
	
	public abstract E clone();
	
//	@Override
//	public boolean uniSameAs(Element element) throws LookupException {
//		boolean result = false;
//		if(element instanceof ActualTypeArgument) {
//			return upperBound().sameAs(((ActualTypeArgument) element).upperBound())
//			      && lowerBound().sameAs(((ActualTypeArgument) element).lowerBound());
//		}
//		return result;
//	}
	
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

//	public boolean alwaysSameAs(ActualTypeArgument argument) throws LookupException {
//		return false;
//	}

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
	
//	public TypeReference intersection(TypeReference other) {
//		return other.intersectionDoubleDispatch(this);
//	}
//
//	public TypeReference intersectionDoubleDispatch(TypeReference other) {
//		return language(ObjectOrientedLanguage.class).createIntersectionReference(clone(), other.clone());
//	}
//
//	public TypeReference intersectionDoubleDispatch(IntersectionTypeReference<?> other) {
//		IntersectionTypeReference<?> result = other.clone();
//		result.add(clone());
//		return result;
//	}

	protected TypeConstraint cloneAndResetTypeReference(TypeConstraint constraint, Element lookupParent) {
		ObjectOrientedLanguage language = language(ObjectOrientedLanguage.class);
		TypeConstraint kloon = constraint.clone();
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
