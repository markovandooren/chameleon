package be.kuleuven.cs.distrinet.chameleon.oo.type;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;

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
	public UnionTypeReference cloneSelf() {
		return new UnionTypeReference();
	}

	@Override
	public Verification verifySelf() {
		return Valid.create();
	}

	public TypeReference intersection(TypeReference other) {
		return other.intersectionDoubleDispatch(this);
	}

	public TypeReference intersectionDoubleDispatch(TypeReference other) {
		UnionTypeReference result = clone(this);
		result.add(clone(other));
		return result;	
	}

	public TypeReference intersectionDoubleDispatch(IntersectionTypeReference other) {
		IntersectionTypeReference result = clone(other);
		result.add(clone(this));
		return result;
	}

	@Override
	public String operatorName() {
		return " U ";
	}

}
