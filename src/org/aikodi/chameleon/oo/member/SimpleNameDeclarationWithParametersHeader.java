package org.aikodi.chameleon.oo.member;

import java.util.List;

import org.aikodi.chameleon.core.Config;
import org.aikodi.chameleon.core.declaration.Signature;
import org.aikodi.chameleon.core.validation.BasicProblem;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.oo.type.TypeReference;
import org.aikodi.chameleon.oo.variable.FormalParameter;

public class SimpleNameDeclarationWithParametersHeader extends DeclarationWithParametersHeader {

  public SimpleNameDeclarationWithParametersHeader(String name) {
    setName(name);
  }
  
  public String getName() {
    return _name;
  }
  
  @Override
public void setName(String name) {
    _name = name;
  }
  
  private String _name;

	@Override
	public SimpleNameDeclarationWithParametersHeader cloneSelf() {
		return new SimpleNameDeclarationWithParametersHeader(getName());
	}

	private SignatureWithParameters _signatureCache;
	
	@Override
	public SignatureWithParameters signature() {
	   SignatureWithParameters result;
		boolean cacheSignatures = Config.cacheSignatures();
		if(cacheSignatures) {
		  result = _signatureCache;
		} else {
			result = null;
		}
		if(result == null) {
			result = new SignatureWithParameters(getName()) {

				@Override
				public void setName(String name) {
					super.setName(name);
					SimpleNameDeclarationWithParametersHeader.this.setName(name);
				}
				
			};
			result.setUniParent(parent());
			for(FormalParameter param: formalParameters()) {
				result.add(clone(param.getTypeReference()));
			}
			if(cacheSignatures) {
				_signatureCache = result;
			}
		}
		return result;
	}

	@Override
	public void flushLocalCache() {
		_signatureCache = null;
	}

	@Override
	public Verification verifySelf() {
		Verification result = Valid.create();
		if(getName() == null) {
			result = result.and(new BasicProblem(this, "The method has no name"));
		}
		return result;
	}

	@Override
	public SimpleNameDeclarationWithParametersHeader createFromSignature(Signature signature) {
		if(signature instanceof SignatureWithParameters) {
		   SignatureWithParameters sig = (SignatureWithParameters) signature;
			SimpleNameDeclarationWithParametersHeader result;
			List<TypeReference> typeReferences = sig.typeReferences();
			List<FormalParameter> params = formalParameters();
			int size = params.size();
			if(typeReferences.size() != size) {
				throw new ChameleonProgrammerException();
			} else {
				// clone and copy parameter types
				result = (SimpleNameDeclarationWithParametersHeader) clone();
				result.setName(sig.name());
				params = result.formalParameters();
				for(int i=0; i <size; i++) {
					params.get(i).setTypeReference(clone(typeReferences.get(i)));
				}
			}
			return result;
		} else {
  		throw new ChameleonProgrammerException("Setting wrong type of signature. Provided: "+(signature == null ? null :signature.getClass().getName())+" Expected SimpleNameSignature");
		}
	}

}
