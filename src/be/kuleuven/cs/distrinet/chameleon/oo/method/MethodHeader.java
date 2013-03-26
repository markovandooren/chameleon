package be.kuleuven.cs.distrinet.chameleon.oo.method;

import be.kuleuven.cs.distrinet.chameleon.oo.member.DeclarationWithParametersHeader;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

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
