package be.kuleuven.cs.distrinet.chameleon.oo.type.generics;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.oo.language.ObjectOrientedLanguage;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;

public class ExtendsWildcard extends ActualTypeArgumentWithTypeReference {

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
	protected ExtendsWildcard cloneSelf() {
		return new ExtendsWildcard(null);
	}

	@Override
	public Type lowerBound() throws LookupException {
		Type baseType = baseType();
		return baseType.language(ObjectOrientedLanguage.class).getNullType(view().namespace());
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
		CapturedTypeParameter newParameter = new CapturedTypeParameter(clone(formal.signature()));
		ObjectOrientedLanguage language = language(ObjectOrientedLanguage.class);
		for(TypeConstraint constraint: formal.constraints()) {
			TypeConstraint clone = cloneAndResetTypeReference(constraint,constraint);
			newParameter.addConstraint(clone);
			accumulator.add(clone);
		}
		newParameter.addConstraint(cloneAndResetTypeReference(new ExtendsConstraint(clone(typeReference())),this));
    return newParameter;
	}

	@Override
	public String toString() {
		TypeReference tref = typeReference();
		StringBuffer result = new StringBuffer();
		result.append('?');
		if(tref != null) {
			result.append(" extends ");
			result.append(toStringTypeReference());
		}
		return result.toString();
	}
	
}
