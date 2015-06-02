package org.aikodi.chameleon.oo.type;

import java.util.ArrayList;
import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.plugin.ObjectOrientedFactory;

public class IntersectionTypeReference extends CombinationTypeReference implements TypeReference {

	public IntersectionTypeReference() {
	}

	public IntersectionTypeReference(List<? extends TypeReference> refs) {
		addAll(refs);
	}
	
	@Override
   public Declaration getDeclarator() throws LookupException {
		throw new LookupException("Requesting declarator for an intersection type reference.");
	}

	@Override
   public Type getElement() throws LookupException {
		List<Type> types = new ArrayList<Type>();
		for(TypeReference ref: typeReferences()) {
			types.add(ref.getElement());
		}
		Type result = language().plugin(ObjectOrientedFactory.class).createIntersectionType(types);
		return result;
	}

	@Override
	protected IntersectionTypeReference cloneSelf() {
		return new IntersectionTypeReference();
	}

	@Override
	public Verification verifySelf() {
		return Valid.create();
	}

	@Override
   public TypeReference intersectionDoubleDispatch(TypeReference other) {
		IntersectionTypeReference result = clone(this);
		result.add(clone(other));
		return result;	
	}

	@Override
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
