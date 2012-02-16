package chameleon.oo.type.generics;

import java.util.ArrayList;
import java.util.List;

import org.rejuse.association.SingleAssociation;

import chameleon.core.element.Element;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.type.TypeReference;


public abstract class ActualTypeArgumentWithTypeReference extends ActualTypeArgument {

	public ActualTypeArgumentWithTypeReference(TypeReference ref) {
		setTypeReference(ref);
	}
	
	public List<? extends Element> children() {
		List<Element> result = new ArrayList<Element>();
		result.add(typeReference());
		return result;
	}

	public TypeReference typeReference() {
		return _type.getOtherEnd();
	}

	public void setTypeReference(TypeReference ref) {
		setAsParent(_type,ref);
	}

	protected SingleAssociation<ActualTypeArgument,TypeReference> _type = new SingleAssociation<ActualTypeArgument,TypeReference>(this);

	@Override
	public VerificationResult verifySelf() {
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

}
