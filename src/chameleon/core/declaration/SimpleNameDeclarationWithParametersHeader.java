package chameleon.core.declaration;

import java.util.List;

import chameleon.core.Config;
import chameleon.core.namespace.NamespaceElement;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.core.variable.FormalParameter;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.oo.type.TypeReference;

public class SimpleNameDeclarationWithParametersHeader<E extends SimpleNameDeclarationWithParametersHeader, P extends NamespaceElement, S extends SimpleNameDeclarationWithParametersSignature> extends DeclarationWithParametersHeader<E, P, S>{

  public SimpleNameDeclarationWithParametersHeader(String name) {
    setName(name);
  }
  
  public String getName() {
    return _name;
  }
  
  public void setName(String name) {
    _name = name;
  }
  
  private String _name;

	@Override
	public E cloneThis() {
		return (E) new SimpleNameDeclarationWithParametersHeader(getName());
	}

	private SimpleNameDeclarationWithParametersSignature _signatureCache;
	
	@Override
	public S signature() {
		SimpleNameDeclarationWithParametersSignature result;
		boolean cacheSignatures = Config.cacheSignatures();
		if(cacheSignatures) {
		  result = _signatureCache;
		} else {
			result = null;
		}
		if(result == null) {
			result = new SimpleNameDeclarationWithParametersSignature(getName()) {

				@Override
				public void setName(String name) {
					super.setName(name);
					SimpleNameDeclarationWithParametersHeader.this.setName(name);
				}
				
			};
			result.setUniParent(parent());
			for(FormalParameter param: formalParameters()) {
				result.add(param.getTypeReference().clone());
			}
			if(cacheSignatures) {
				_signatureCache = result;
			}
		}
		return (S) result;
	}

	@Override
	public void flushLocalCache() {
		_signatureCache = null;
	}

	@Override
	public VerificationResult verifySelf() {
		VerificationResult result = Valid.create();
		if(getName() == null) {
			result = result.and(new BasicProblem(this, "The method has no name"));
		}
		return result;
	}

	@Override
	public E createFromSignature(Signature signature) {
		if(signature instanceof SimpleNameDeclarationWithParametersSignature) {
			SimpleNameDeclarationWithParametersSignature sig = (SimpleNameDeclarationWithParametersSignature) signature;
			E result;
			List<TypeReference> typeReferences = sig.typeReferences();
			List<FormalParameter> params = formalParameters();
			int size = params.size();
			if(typeReferences.size() != size) {
				throw new ChameleonProgrammerException();
			} else {
				// clone and copy parameter types
				result = clone();
				result.setName(sig.name());
				params = result.formalParameters();
				for(int i=0; i <size; i++) {
					params.get(i).setTypeReference(typeReferences.get(i).clone());
				}
			}
			return result;
		} else {
  		throw new ChameleonProgrammerException("Setting wrong type of signature. Provided: "+(signature == null ? null :signature.getClass().getName())+" Expected SimpleNameSignature");
		}
	}

	

}
