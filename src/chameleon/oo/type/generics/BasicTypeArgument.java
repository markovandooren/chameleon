package chameleon.oo.type.generics;

import java.util.List;

import chameleon.core.lookup.LookupException;
import chameleon.oo.language.ObjectOrientedLanguage;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeReference;
import chameleon.util.CreationStackTrace;
import chameleon.util.Pair;

/**
 * A class of type arguments that consist of a type name.
 * 
 * @author Marko van Dooren
 */
public class BasicTypeArgument<E extends BasicTypeArgument> extends ActualTypeArgumentWithTypeReference<E> {

	private CreationStackTrace _trace = new CreationStackTrace();
	
	public BasicTypeArgument(TypeReference ref) {
		super(ref);
	}

	@Override
	public E clone() {
		return (E) new BasicTypeArgument(typeReference().clone());
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
		CapturedTypeParameter newParameter = new CapturedTypeParameter(formal.signature().clone());
		TypeReference typeReference = typeReference();
		TypeReference clone = typeReference.clone();
		clone.setOrigin(typeReference);//DEBUG
		TypeReference nl = language(ObjectOrientedLanguage.class).createNonLocalTypeReference(clone, this);
		newParameter.addConstraint(new EqualityConstraint(nl));
		return newParameter;
	}

	@Override
	public TypeReference substitutionReference() {
		return typeReference();
	}

}
