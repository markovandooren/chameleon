package be.kuleuven.cs.distrinet.chameleon.oo.type;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;

public class IntersectionTypeReference extends CombinationTypeReference implements TypeReference {

	public IntersectionTypeReference() {
	}

	public IntersectionTypeReference(List<? extends TypeReference> refs) {
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
		Type result = IntersectionType.create(types);
		return result;
	}

	@Override
	public IntersectionTypeReference clone() {
		List<TypeReference> trefs = new ArrayList<TypeReference>();
		for(TypeReference tref: typeReferences()) {
			trefs.add(tref.clone());
		}
		return new IntersectionTypeReference(trefs);
	}

	@Override
	public Verification verifySelf() {
		return Valid.create();
	}

	public TypeReference intersection(TypeReference other) {
		return other.intersectionDoubleDispatch(this);
	}

	public TypeReference intersectionDoubleDispatch(TypeReference other) {
		IntersectionTypeReference result = clone();
		result.add(other.clone());
		return result;	
	}

	public TypeReference intersectionDoubleDispatch(IntersectionTypeReference other) {
		IntersectionTypeReference result = clone();
		for(TypeReference tref: other.typeReferences()) {
		  result.add(tref.clone());
		}
		return result;	
	}

	@Override
	public String operatorName() {
		return " & ";
	}

}
