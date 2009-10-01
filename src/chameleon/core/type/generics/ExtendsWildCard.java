package chameleon.core.type.generics;

import chameleon.core.language.ObjectOrientedLanguage;
import chameleon.core.lookup.LookupException;
import chameleon.core.type.Type;
import chameleon.core.type.TypeReference;

public class ExtendsWildCard extends ActualTypeArgumentWithTypeReference<ExtendsWildCard> {

	public ExtendsWildCard(TypeReference ref) {
		super(ref);
	}

	@Override
	public Type type() throws LookupException {
		return new ExtendsWildcardType(baseType());
	}

	@Override
	public ExtendsWildCard clone() {
		return new ExtendsWildCard(typeReference().clone());
	}

//	@Override
//	public boolean contains(ActualTypeArgument other) throws LookupException {
//		return (
//				     (other instanceof BasicTypeArgument) && 
//				     (((BasicTypeArgument)other).type().equals(baseType()))
//				   ) ||
//				   (
//				  	 (other instanceof ExtendsWildCard) &&
//				  	 (((ExtendsWildCard)other).baseType().subTypeOf(baseType()))
//				   );
//	}

	@Override
	public Type lowerBound() throws LookupException {
		return baseType().language(ObjectOrientedLanguage.class).getNullType();
	}

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
	public TypeParameter capture(FormalTypeParameter formal) {
		CapturedTypeParameter newParameter = new CapturedTypeParameter(formal.signature().clone());
		for(TypeConstraint constraint: formal.constraints()) {
			newParameter.addConstraint(constraint.clone());
		}
		newParameter.addConstraint(new ExtendsConstraint(typeReference().clone()));
    return newParameter;
	}

}
