package org.aikodi.chameleon.oo.type.generics;

import java.util.List;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeFixer;
import org.aikodi.chameleon.oo.type.TypeReference;
import org.aikodi.chameleon.util.Util;

/**
 * A class of type arguments that consist of a type name.
 * 
 * @author Marko van Dooren
 */
public class EqualityTypeArgument extends TypeArgumentWithTypeReference {

	public EqualityTypeArgument(TypeReference ref) {
		super(ref);
	}

	@Override
	protected EqualityTypeArgument cloneSelf() {
		return new EqualityTypeArgument(null);
	}

	/*@
   @ public behavior
   @
   @ post \result == baseType();
   @*/
	@Override
	public Type type() throws LookupException {
		return baseType();
	}

	/*@
   @ public behavior
   @
   @ post \result == baseType();
   @*/
	@Override
	public Type lowerBound() throws LookupException {
		return baseType();
	}

	/*@
   @ public behavior
   @
   @ post \result == baseType();
   @*/
	@Override
	public Type upperBound() throws LookupException {
		return baseType();
	}

	public Type baseType() throws LookupException {
		TypeReference tref = typeReference();
		if(tref != null) {
			Type type = tref.getElement();
			if(type != null) {
				return type;
			} else {
				throw new LookupException("Lookup of type of generic argument return null."); 
			}
		} else {
			throw new LookupException("Generic argument has no type reference.");
		}
	}

	@Override
	public TypeParameter capture(FormalTypeParameter formal, List<TypeConstraint> accumulator) {
//		return clone(nearestAncestor(TypeParameter.class));
		InstantiatedTypeParameter newParameter = new InstantiatedTypeParameter(formal.name(), this);
//		TypeReference typeReference = typeReference();
//		TypeReference clone = clone(typeReference);
//		clone.setOrigin(typeReference);//DEBUG //FIXME remove debugging statement after clone has been refactored
//		TypeReference nl = language(ObjectOrientedLanguage.class).createNonLocalTypeReference(clone, this);
//		newParameter.addConstraint(new EqualityConstraint(nl));
		return newParameter;
	}

	@Override
	public TypeReference substitutionReference() {
		return typeReference();
	}

	@Override
	public String toString(java.util.Set<Element> visited) {
		return toStringTypeReference(visited);
	}

	//	@Override
	//	public boolean contains(TypeArgument other, TypeFixer trace) throws LookupException {
	//		tracer.push();
	//		boolean result = other instanceof EqualityTypeArgument;
	//		if(result) {
	//			Type otherType = ((EqualityTypeArgument)other).baseType();
	//			Type myType = baseType();
	//			result = myType.upperBoundNotHigherThan(otherType,trace);
	//			if(result) {
	//				result = otherType.upperBoundNotHigherThan(myType,trace);
	//			}
	//		}
	//		tracer.pop();
	//		return result;
	//	}


	/* (non-Javadoc)
	 * @see org.aikodi.chameleon.oo.type.generics.TypeArgument#contains(org.aikodi.chameleon.oo.type.generics.TypeParameter, org.aikodi.chameleon.oo.type.TypeFixer)
	 */
	@Override
	public boolean contains(TypeParameter other, TypeFixer trace) throws LookupException {
//		TypeFixer f = trace.clone();
//		boolean zuppa = super.contains(other, f);
//		
		Type otherType = other.selectionDeclaration();
		boolean result = baseType().subtypeOf(otherType, trace);
		if(result) {
			result = otherType.subtypeOf(baseType(), trace);
		}
//		Util.debug(zuppa != result);
		return result;
	}

	@Override
	public boolean isWildCardBound() throws LookupException {
		return false;
		//		return baseType().isWildCard();
	}

}
