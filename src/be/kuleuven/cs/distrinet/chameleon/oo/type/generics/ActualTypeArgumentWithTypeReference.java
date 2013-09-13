package be.kuleuven.cs.distrinet.chameleon.oo.type.generics;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.validation.BasicProblem;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.oo.type.BasicTypeReference;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;


public abstract class ActualTypeArgumentWithTypeReference extends ActualTypeArgument {

	public ActualTypeArgumentWithTypeReference(TypeReference ref) {
		setTypeReference(ref);
	}
	
	public TypeReference typeReference() {
		return _type.getOtherEnd();
	}

	public void setTypeReference(TypeReference ref) {
		set(_type,ref);
	}

	protected Single<TypeReference> _type = new Single<TypeReference>(this);

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
					String replacement = ((InstantiatedParameterType)element).parameter().toString();
					tref.setName(replacement);
				}
			}
			return clone.toString();
		} catch (LookupException e) {
			return typeReference().toString();
		}
	}

}
