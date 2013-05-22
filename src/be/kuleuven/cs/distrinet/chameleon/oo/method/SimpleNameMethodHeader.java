package be.kuleuven.cs.distrinet.chameleon.oo.method;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.Config;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Signature;
import be.kuleuven.cs.distrinet.chameleon.core.validation.BasicProblem;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.chameleon.oo.member.SimpleNameDeclarationWithParametersSignature;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;
import be.kuleuven.cs.distrinet.chameleon.oo.variable.FormalParameter;

/**
 * 
 * @author Marko van Dooren
 */
public class SimpleNameMethodHeader extends MethodHeader {

	//FIXME LARGELY COPIED FROM SimpleNameDeclarationWithParametersHeader.
	//      Better solution is to use a SimpleNameDeclarationWithParametersHeader as a subobject, but
	//      I don't want to increase memory consumption further until we have lazy class loading. A
	//      tool that uses too much memory and starts to swap is completely useless.

	public SimpleNameMethodHeader(String name, TypeReference returnType) {
		super(returnType);
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
	public SimpleNameMethodHeader cloneSelf() {
		return new SimpleNameMethodHeader(getName(), null);
	}

	private SimpleNameDeclarationWithParametersSignature _signatureCache;
	
	@Override
	public SimpleNameDeclarationWithParametersSignature signature() {
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
					SimpleNameMethodHeader.this.setName(name);
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
	public SimpleNameMethodHeader createFromSignature(Signature signature) {
		if(signature instanceof SimpleNameDeclarationWithParametersSignature) {
			SimpleNameDeclarationWithParametersSignature sig = (SimpleNameDeclarationWithParametersSignature) signature;
			SimpleNameMethodHeader result;
			List<TypeReference> typeReferences = sig.typeReferences();
			List<FormalParameter> params = formalParameters();
			int size = params.size();
			if(typeReferences.size() != size) {
				throw new ChameleonProgrammerException();
			} else {
				// clone and copy parameter types
				result = (SimpleNameMethodHeader) clone();
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
