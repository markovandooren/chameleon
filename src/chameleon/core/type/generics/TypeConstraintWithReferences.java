package chameleon.core.type.generics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.rejuse.association.OrderedMultiAssociation;
import org.rejuse.association.SingleAssociation;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.type.Type;
import chameleon.core.type.TypeReference;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.util.Util;

public abstract class TypeConstraintWithReferences<E extends TypeConstraintWithReferences> extends TypeConstraint<E> {

	
	public void setTypeReference(TypeReference ref) {
		if(ref != null) {
			_types.connectTo(ref.parentLink());
		} else {
			_types.connectTo(null);
		}
	}
	public TypeReference typeReference() {
		return _types.getOtherEnd();
	}
	
	private SingleAssociation<TypeConstraintWithReferences,TypeReference> _types = new SingleAssociation<TypeConstraintWithReferences, TypeReference>(this);

	public List<Element> children() {
		return Util.createNonNullList(typeReference());
	} 
	
	@Override
	public E clone() {
		E result = cloneThis();
		result.setTypeReference(typeReference().clone());
		return result;
	}
	
	public abstract E cloneThis();

	public Type bound() throws LookupException {
		return typeReference().getElement();
	}

	@Override
	public VerificationResult verifySelf() {
		if(typeReference() != null) {
			return Valid.create();
		} else {
			return new MissingConstraintTypes(this);
		}
	}
	
	public static class MissingConstraintTypes extends BasicProblem {

		public MissingConstraintTypes(Element element) {
			super(element, "The type constraint contains no type names");
		}
		
	}

}
