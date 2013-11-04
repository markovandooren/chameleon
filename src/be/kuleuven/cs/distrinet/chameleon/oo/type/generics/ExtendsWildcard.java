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

	/**
	 * For wildcards {@code ? extends B} corresponding to parameter {@code Fi extends Ui}, the captured type parameter 
	 * is Si with upper bound {@code glb(B, Ui[A1=S1,...,Ai=Si,...])} and lower bound the null type.
	 * 
	 * All constraints of the formal parameter are copied, and with a non-local reference, their
	 * lookup path is redirected to the formal parameter such that all types in the bounds Ui are
	 * resolved corrected. An additional constraint
	 * with B as the upper bound is added, and its lookup path is redirected to this wildcard object
	 * to correctly resolve any types in B.
	 * 
	 * <b>Any references to Ai must still replaced by references to Si. Capture conversion for 
	 * derived types takes care of this.</b>  
	 * 
	 * Note that in practice, the name of the parameter is unchanged. It is not needed because we use
	 * an object model instead of a string match as is typically done in type system. 
	 */
	@Override
	public TypeParameter capture(FormalTypeParameter formal, List<TypeConstraint> accumulator) {
		CapturedTypeParameter newParameter = new CapturedTypeParameter(formal.name());
		// Copy and redirect the bound of the formal parameter.
		for(TypeConstraint constraint: formal.constraints()) {
			TypeConstraint clone = cloneAndResetTypeReference(constraint,constraint);
			newParameter.addConstraint(clone);
			accumulator.add(clone);
		}
		// Copy and redirect the type argument
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
