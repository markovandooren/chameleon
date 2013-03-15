package chameleon.oo.type.generics;

import java.util.List;

import be.kuleuven.cs.distrinet.rejuse.association.SingleAssociation;
import chameleon.core.element.Element;
import chameleon.core.element.ElementImpl;
import chameleon.core.lookup.LookupException;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeReference;
import chameleon.util.Util;

public abstract class TypeConstraint extends ElementImpl {

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
		set(_types,ref);
	}
	
	public TypeReference typeReference() {
		return _types.getOtherEnd();
	}
	
	private SingleAssociation<TypeConstraint,TypeReference> _types = new SingleAssociation<TypeConstraint, TypeReference>(this);

	@Override
	public TypeConstraint clone() {
		TypeConstraint result = cloneThis();
		result.setTypeReference(typeReference().clone());
		return result;
	}
	
	public abstract TypeConstraint cloneThis();

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
