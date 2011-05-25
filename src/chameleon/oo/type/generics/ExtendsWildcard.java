package chameleon.oo.type.generics;

import java.util.List;

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
	public TypeParameter capture(FormalTypeParameter formal, List<TypeConstraint> accumulator) {
		CapturedTypeParameter newParameter = new CapturedTypeParameter(formal.signature().clone());
		ObjectOrientedLanguage language = language(ObjectOrientedLanguage.class);
		for(TypeConstraint constraint: formal.constraints()) {
			TypeConstraint clone = cloneAndResetTypeReference(constraint,constraint);
			newParameter.addConstraint(clone);
			accumulator.add(clone);
		}
		newParameter.addConstraint(cloneAndResetTypeReference(new ExtendsConstraint(typeReference().clone()),this));
    return newParameter;
	}

	@Override
	public String infoDisplayName() {
		TypeReference tref = typeReference();
		StringBuffer result = new StringBuffer();
		result.append('?');
		if(tref != null) {
			result.append(" extends ");
			result.append(typeReference().infoDisplayName());
		}
		return result.toString();
	}
	
}
