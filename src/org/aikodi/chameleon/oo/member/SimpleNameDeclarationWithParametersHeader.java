package org.aikodi.chameleon.oo.member;

import org.aikodi.chameleon.core.declaration.Signature;
import org.aikodi.chameleon.core.event.name.NameChanged;
import org.aikodi.chameleon.core.validation.BasicProblem;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.oo.type.TypeReference;
import org.aikodi.chameleon.oo.variable.FormalParameter;

import java.util.List;

public class SimpleNameDeclarationWithParametersHeader extends DeclarationWithParametersHeader {

  public SimpleNameDeclarationWithParametersHeader(String name) {
    setName(name);
  }
  
  public String getName() {
    return _name;
  }
  
  @Override
  public void setName(String name) {
    String old = _name;
    doSetName(name);
    if(changeNotificationEnabled()) {
      notify(new NameChanged(old, name));
    }
  }

  protected void doSetName(String name) {
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
		SignatureWithParameters result = _signatureCache;
		if(result == null) {
			result = new SignatureWithParameters(getName()) {

				@Override
				public void setName(String name) {
					super.setName(name);
					SimpleNameDeclarationWithParametersHeader.this.doSetName(name);
				}
				
			};
			result.setUniParent(parent());
			for(FormalParameter param: formalParameters()) {
				result.add(clone(param.getTypeReference()));
			}
			_signatureCache = result;
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
