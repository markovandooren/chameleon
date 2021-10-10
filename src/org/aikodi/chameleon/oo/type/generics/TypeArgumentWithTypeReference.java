package org.aikodi.chameleon.oo.type.generics;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.validation.BasicProblem;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.type.BasicTypeReference;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeReference;
import org.aikodi.chameleon.util.association.Single;

import java.util.List;
import java.util.Set;


public abstract class TypeArgumentWithTypeReference extends TypeArgument {

	public TypeArgumentWithTypeReference(TypeReference ref) {
		setTypeReference(ref);
	}

	public TypeReference typeReference() {
		return _type.getOtherEnd();
	}

	public void setTypeReference(TypeReference ref) {
		set(_type,ref);
	}

	private Single<TypeReference> _type = new Single<TypeReference>(this);

	@Override
	public Verification verifySelf() {
		if(typeReference() != null) {
			return Valid.create();
		} else {
			return new MissingTypeReference(this);
		}
	}

	public static class MissingTypeReference extends BasicProblem {

		public MissingTypeReference(Element element) {
			super(element, "Missing type reference");
		}

	}

	protected String toStringTypeReference(Set<Element> visited) {
		try {
			TypeReference clone = clone(typeReference());
			clone.setUniParent(this);
			List<BasicTypeReference> descendants = clone.lexical().descendants(BasicTypeReference.class);
			if(clone instanceof BasicTypeReference) {
				descendants.add((BasicTypeReference) clone);
			}
			for(BasicTypeReference tref: descendants) {
				Type element = tref.getElement();
				if(element instanceof InstantiatedParameterType) {
					String replacement = ((InstantiatedParameterType)element).parameter().toString(visited);
					tref.setName(replacement);
				}
			}
			return clone.toString(visited);
		} catch (Exception e) {
			return typeReference().toString();
		}
	}

	protected String toStringTypeReferenceXXX() {
		return typeReference().toString();
	}
}
