package org.aikodi.chameleon.oo.type.generics;

import java.util.HashSet;
import java.util.Set;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.type.BasicTypeReference;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeReference;
import org.aikodi.chameleon.util.association.Single;

/**
 * A class of constraints that determine which types are valid
 * arguments for {@link TypeParameter}s.
 * 
 * @author Marko van Dooren
 */
public abstract class TypeConstraint extends ElementImpl {

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
		set(_typeReference,ref);
	}
	
	public TypeReference typeReference() {
		return _typeReference.getOtherEnd();
	}
	
	private Single<TypeReference> _typeReference = new Single<TypeReference>(this, true);

	protected Type bound() throws LookupException {
		return typeReference().getElement();
	}

	protected String toStringTypeReference(Set<Element> visited) {
		try {
			TypeReference clone = clone(typeReference());
			clone.setUniParent(this);
			java.util.List<BasicTypeReference> descendants = clone.descendants(BasicTypeReference.class);
			if(clone instanceof BasicTypeReference) {
				descendants.add((BasicTypeReference) clone);
			}
			for(BasicTypeReference tref: descendants) {
				Type element = tref.getElement();
				if(element instanceof InstantiatedParameterType) {
					TypeParameter parameter = ((InstantiatedParameterType)element).parameter();
					String replacement = parameter.toString(visited);
//					if(parameter instanceof CapturedTypeParameter) {
//						replacement = "";
//						for(TypeConstraint constraint: ((CapturedTypeParameter) parameter).constraints()) {
//							visited.add(parameter);
//							replacement += (constraint.toStringTypeReference(visited)+" ");
//						}
//					}
					tref.setName(replacement);
				}
			}
			return clone.toString(visited);
		} catch (Exception e) {
			return typeReference().toString();
		}
	}
	
	public abstract TypeArgument argument();

	@Override
	public final String toString() {
	  return toString(new HashSet<>());
	}
	
	/**
	 * @param visited
	 * @return
	 */
	public abstract String toString(Set<Element> visited);
}
