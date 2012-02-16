package chameleon.oo.type;

import java.util.ArrayList;
import java.util.List;


import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.Signature;
import chameleon.core.lookup.LookupException;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.exception.ChameleonProgrammerException;

public class UnionTypeReference extends CombinationTypeReference {
//FIXME make abstract superclass for this and IntersectionTypeReference
	public UnionTypeReference() {
		
	}

	public UnionTypeReference(List<? extends TypeReference> refs) {
		addAll(refs);
	}
	
	public Declaration getDeclarator() throws LookupException {
		throw new LookupException("Requesting declarator for an intersection type reference.");
	}

	public Type getElement() throws LookupException {
		List<Type> types = new ArrayList<Type>();
		for(TypeReference ref: typeReferences()) {
			types.add(ref.getElement());
		}
		Type result = UnionType.create(types);
		result.setUniParent(this);
		return result;
	}

	@Override
	public UnionTypeReference clone() {
		List<TypeReference> trefs = new ArrayList<TypeReference>();
		for(TypeReference tref: typeReferences()) {
			trefs.add(tref.clone());
		}
		return new UnionTypeReference(trefs);
	}

	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}

	public TypeReference intersection(TypeReference other) {
		return other.intersectionDoubleDispatch(this);
	}

	public TypeReference intersectionDoubleDispatch(TypeReference other) {
		UnionTypeReference result = clone();
		result.add(other.clone());
		return result;	
	}

	public TypeReference intersectionDoubleDispatch(IntersectionTypeReference other) {
		IntersectionTypeReference result = other.clone();
		result.add(clone());
		return result;
	}

	@Override
	public String operatorName() {
		return " U ";
	}

}