package chameleon.oo.method;

import chameleon.oo.member.DeclarationWithParametersHeader;
import chameleon.oo.type.TypeReference;
import chameleon.util.association.Single;

public abstract class MethodHeader extends DeclarationWithParametersHeader {

	public MethodHeader(TypeReference returnType) {
		setReturnTypeReference(returnType);
	}
	
//	public abstract MethodHeader clone();
	
	private Single<TypeReference> _typeReference = new Single<TypeReference>(this);

	public TypeReference returnTypeReference() {
		return _typeReference.getOtherEnd();
	}

	public void setReturnTypeReference(TypeReference type) {
		set(_typeReference,type);
	}

}
