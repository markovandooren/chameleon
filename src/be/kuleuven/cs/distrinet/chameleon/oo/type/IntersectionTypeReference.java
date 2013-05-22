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
	public IntersectionTypeReference cloneSelf() {
		return new IntersectionTypeReference();
	}

	@Override
	public Verification verifySelf() {
		return Valid.create();
	}

	public TypeReference intersection(TypeReference other) {
		return other.intersectionDoubleDispatch(this);
	}

	public TypeReference intersectionDoubleDispatch(TypeReference other) {
		IntersectionTypeReference result = clone(this);
		result.add(clone(other));
		return result;	
	}

	public TypeReference intersectionDoubleDispatch(IntersectionTypeReference other) {
		IntersectionTypeReference result = clone(this);
		for(TypeReference tref: other.typeReferences()) {
		  result.add(clone(tref));
		}
		return result;	
	}

	@Override
	public String operatorName() {
		return " & ";
	}

}
