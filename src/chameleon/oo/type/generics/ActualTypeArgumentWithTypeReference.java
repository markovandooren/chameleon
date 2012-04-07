package chameleon.oo.type.generics;

import chameleon.core.element.Element;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.type.TypeReference;
import chameleon.util.association.Single;


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
