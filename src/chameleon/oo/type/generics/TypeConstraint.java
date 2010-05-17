package chameleon.oo.type.generics;

import java.util.List;

import org.rejuse.association.SingleAssociation;

import chameleon.core.element.Element;
import chameleon.core.element.ElementImpl;
import chameleon.core.lookup.LookupException;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeReference;
import chameleon.util.Util;

public abstract class TypeConstraint<E extends TypeConstraint> extends ElementImpl<E,Element> {

	public TypeConstraint() {
	}
	
	public abstract boolean matches(Type type) throws LookupException;
	
	/**
	 * Return the upper bound on the type that this type constraint imposes.
	 * 
	 * @return
	 * @throws LookupException 
	 */
	public abstract Type upperBound() throws LookupException;
	
	public abstract TypeReference upperBoundReference();
	
	/**
	 * Return the lower bound on the type that this type constraint imposes.
	 * 
	 * @return
	 * @throws LookupException 
	 */
	public abstract Type lowerBound() throws LookupException;
	
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
	
	private SingleAssociation<TypeConstraint,TypeReference> _types = new SingleAssociation<TypeConstraint, TypeReference>(this);

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
