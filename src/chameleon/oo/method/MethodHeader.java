package chameleon.oo.method;

import org.rejuse.association.SingleAssociation;

import chameleon.oo.member.DeclarationWithParametersHeader;
import chameleon.oo.type.TypeReference;

public abstract class MethodHeader extends DeclarationWithParametersHeader {

	public MethodHeader(TypeReference returnType) {
		setReturnTypeReference(returnType);
	}
	
//	public abstract MethodHeader clone();
	
	private SingleAssociation<MethodHeader,TypeReference> _typeReference = new SingleAssociation<MethodHeader,TypeReference>(this);

	public TypeReference returnTypeReference() {
		return _typeReference.getOtherEnd();
	}

	public void setReturnTypeReference(TypeReference type) {
		setAsParent(_typeReference,type);
	}

}
