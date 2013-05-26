package be.kuleuven.cs.distrinet.chameleon.oo.type.generics;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.oo.language.ObjectOrientedLanguage;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;

/**
 * A class of type arguments that consist of a type name.
 * 
 * @author Marko van Dooren
 */
public class BasicTypeArgument extends ActualTypeArgumentWithTypeReference {

	public BasicTypeArgument(TypeReference ref) {
		super(ref);
	}

	@Override
	protected BasicTypeArgument cloneSelf() {
		return new BasicTypeArgument(null);
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
			Type type = tref.getType();
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
		CapturedTypeParameter newParameter = new CapturedTypeParameter(clone(formal.signature()));
		TypeReference typeReference = typeReference();
		TypeReference clone = clone(typeReference);
		clone.setOrigin(typeReference);//DEBUG //FIXME remove debugging statement after clone has been refactored
		TypeReference nl = language(ObjectOrientedLanguage.class).createNonLocalTypeReference(clone, this);
		newParameter.addConstraint(new EqualityConstraint(nl));
		return newParameter;
	}

	@Override
	public TypeReference substitutionReference() {
		return typeReference();
	}

	@Override
	public String toString() {
		return typeReference().toString();
	}

}
