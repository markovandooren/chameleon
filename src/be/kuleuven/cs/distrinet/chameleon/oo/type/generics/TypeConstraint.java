package be.kuleuven.cs.distrinet.chameleon.oo.type.generics;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.element.ElementImpl;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.validation.BasicProblem;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.oo.type.BasicTypeReference;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;
import be.kuleuven.cs.distrinet.rejuse.association.SingleAssociation;

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
	
	private Single<TypeReference> _types = new Single<TypeReference>(this);

//	public abstract TypeConstraint cloneThis();

	public Type bound() throws LookupException {
		return typeReference().getElement();
	}

	@Override
	public Verification verifySelf() {
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

	protected String toStringTypeReference() {
		try {
			TypeReference clone = clone(typeReference());
			clone.setUniParent(this);
			List<BasicTypeReference> descendants = clone.descendants(BasicTypeReference.class);
			if(clone instanceof BasicTypeReference) {
				descendants.add((BasicTypeReference) clone);
			}
			for(BasicTypeReference tref: descendants) {
				Type element = tref.getElement();
				if(element instanceof InstantiatedParameterType) {
					TypeParameter parameter = ((InstantiatedParameterType)element).parameter();
					String replacement = parameter.toString();
					if(parameter instanceof CapturedTypeParameter) {
						replacement = "";
						for(TypeConstraint constraint: ((CapturedTypeParameter) parameter).constraints()) {
							replacement += (constraint.toStringTypeReference()+" ");
						}
					}
					tref.setName(replacement);
				}
			}
			return clone.toString();
		} catch (LookupException e) {
			return typeReference().toString();
		}
	}
}
