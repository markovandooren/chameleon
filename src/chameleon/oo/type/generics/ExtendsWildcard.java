package chameleon.oo.type.generics;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.oo.language.ObjectOrientedLanguage;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeReference;

public class ExtendsWildcard<E extends ExtendsWildcard> extends ActualTypeArgumentWithTypeReference<E> {

	public ExtendsWildcard(TypeReference ref) {
		super(ref);
	}

	@Override
	public Type type() throws LookupException {
		ExtendsWildcardType extendsWildcardType = new ExtendsWildcardType(baseType());
		extendsWildcardType.setUniParent(this);
		return extendsWildcardType;
	}

	@Override
	public E clone() {
		return (E) new ExtendsWildcard(typeReference().clone());
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
		Type baseType = baseType();
		return baseType.language(ObjectOrientedLanguage.class).getNullType();
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
