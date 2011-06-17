package chameleon.core.method;

import org.rejuse.association.SingleAssociation;

import chameleon.core.declaration.DeclarationWithParametersHeader;
import chameleon.core.declaration.DeclarationWithParametersSignature;
import chameleon.core.declaration.Signature;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.type.TypeReference;

public abstract class MethodHeader<E extends DeclarationWithParametersHeader, S extends DeclarationWithParametersSignature> extends DeclarationWithParametersHeader<E,S> {

	public MethodHeader(TypeReference returnType) {
		setReturnTypeReference(returnType);
	}
	
	private SingleAssociation<MethodHeader,TypeReference> _typeReference = new SingleAssociation<MethodHeader,TypeReference>(this);

	public TypeReference returnTypeReference() {
		return _typeReference.getOtherEnd();
	}

	public void setReturnTypeReference(TypeReference type) {
		if(type != null) {
			_typeReference.connectTo(type.parentLink());
		}
		else {
			_typeReference.connectTo(null);
		}
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
