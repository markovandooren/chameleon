package chameleon.oo.method;

import java.util.List;

import org.rejuse.association.SingleAssociation;

import chameleon.core.element.Element;
import chameleon.oo.member.DeclarationWithParametersHeader;
import chameleon.oo.member.DeclarationWithParametersSignature;
import chameleon.oo.type.TypeReference;
import chameleon.util.Util;

public abstract class MethodHeader extends DeclarationWithParametersHeader {

	public MethodHeader(TypeReference returnType) {
		setReturnTypeReference(returnType);
	}
	
	private SingleAssociation<MethodHeader,TypeReference> _typeReference = new SingleAssociation<MethodHeader,TypeReference>(this);

	public TypeReference returnTypeReference() {
		return _typeReference.getOtherEnd();
	}

	public void setReturnTypeReference(TypeReference type) {
		setAsParent(_typeReference,type);
	}

	@Override
	public List<Element> children() {
		List<Element> result = super.children();
		Util.addNonNull(returnTypeReference(), result);
		return result;
	}
	
//  @Override
//  public LookupStrategy lexicalLookupStrategy(Element element) throws LookupException {
//  	if(element.sameAs(returnTypeReference()) || element.isDerived()) {
//  		return lexicalStrategy();
//  	}
//  	else {
//  		return super.lexicalLookupStrategy(element);
//  	}
//  }


}
