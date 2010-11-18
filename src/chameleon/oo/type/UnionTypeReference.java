package chameleon.oo.type;

import java.util.ArrayList;
import java.util.List;

import org.rejuse.association.OrderedMultiAssociation;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.Signature;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespace.NamespaceElementImpl;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.exception.ChameleonProgrammerException;

public class UnionTypeReference<E extends UnionTypeReference> extends NamespaceElementImpl<E, Element> implements TypeReference<E> {
//FIXME make abstract superclass for this and IntersectionTypeReference
	public UnionTypeReference() {
		
	}

	public UnionTypeReference(List<? extends TypeReference> refs) {
		addAll(refs);
	}
	
	protected OrderedMultiAssociation<UnionTypeReference,TypeReference> _types = new OrderedMultiAssociation<UnionTypeReference, TypeReference>(this);

	public Type getType() throws LookupException {
		return getElement();
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

	public List<TypeReference> typeReferences() {
		return _types.getOtherEnds();
	}

	public List<Element> children() {
		return new ArrayList<Element>(typeReferences());
	}

	@Override
	public E clone() {
		List<TypeReference> trefs = new ArrayList<TypeReference>();
		for(TypeReference tref: typeReferences()) {
			trefs.add(tref.clone());
		}
		return (E) new UnionTypeReference(trefs);
	}

	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}

	public TypeReference intersection(TypeReference other) {
		return other.intersectionDoubleDispatch(this);
	}

	public void add(TypeReference tref) {
		if(tref != null) {
			_types.add(tref.parentLink());
		}
	}
	
	public void addAll(List<? extends TypeReference> refs) {
		for(TypeReference ref: refs) {
			add(ref);
		}
	}
	
	public void remove(TypeReference tref) {
		if(tref != null) {
			_types.remove(tref.parentLink());
		}
	}

	public TypeReference intersectionDoubleDispatch(TypeReference other) {
		UnionTypeReference result = clone();
		result.add(other.clone());
		return result;	
	}

	public TypeReference intersectionDoubleDispatch(IntersectionTypeReference<?> other) {
		IntersectionTypeReference<?> result = other.clone();
		result.add(clone());
		return result;
	} 
	
}