package org.aikodi.chameleon.oo.type.generics;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.validation.BasicProblem;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeReference;
import org.aikodi.chameleon.util.association.Single;

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
//		try {
//			TypeReference clone = clone(typeReference());
//			clone.setUniParent(this);
//			List<BasicTypeReference> descendants = clone.descendants(BasicTypeReference.class);
//			if(clone instanceof BasicTypeReference) {
//				descendants.add((BasicTypeReference) clone);
//			}
//			for(BasicTypeReference tref: descendants) {
//				Type element = tref.getElement();
//				if(element instanceof InstantiatedParameterType) {
//					TypeParameter parameter = ((InstantiatedParameterType)element).parameter();
//					String replacement = parameter.toString();
//					if(parameter instanceof CapturedTypeParameter) {
//						replacement = "";
//						for(TypeConstraint constraint: ((CapturedTypeParameter) parameter).constraints()) {
//							replacement += (constraint.toStringTypeReference()+" ");
//						}
//					}
//					tref.setName(replacement);
//				}
//			}
//			return clone.toString();
//		} catch (Exception e) {
			return typeReference().toString();		
	}
}
