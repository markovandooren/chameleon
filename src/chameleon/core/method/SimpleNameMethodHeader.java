package chameleon.core.method;

import java.util.List;

import chameleon.core.Config;
import chameleon.core.declaration.DeclarationWithParametersSignature;
import chameleon.core.declaration.Signature;
import chameleon.core.declaration.SimpleNameDeclarationWithParametersSignature;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.core.variable.FormalParameter;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.oo.type.TypeReference;

/**
 * 
 * @author Marko van Dooren
 *
 * @param <E>
 * @param <S>
 */
public class SimpleNameMethodHeader<E extends SimpleNameMethodHeader, S extends DeclarationWithParametersSignature> extends MethodHeader<E,S> {

	//FIXME LARGELY COPIED FROM SimpleNameDeclarationWithParametersHeader.
	//      Better solution is to use a SimpleNameDeclarationWithParametersHeader as a subobject, but
	//      I don't want to increase memory consumption further until we have lazy class loading. A
	//      tool that uses too much memory is completely useless.
	
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
	public E cloneThis() {
		return (E) new SimpleNameMethodHeader(getName(), returnTypeReference().clone());
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
					SimpleNameMethodHeader.this.setName(name);
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
