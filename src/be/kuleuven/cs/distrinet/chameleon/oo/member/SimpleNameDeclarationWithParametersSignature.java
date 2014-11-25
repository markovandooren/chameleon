package be.kuleuven.cs.distrinet.chameleon.oo.member;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Signature;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.validation.BasicProblem;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;
import be.kuleuven.cs.distrinet.chameleon.util.Lists;
import be.kuleuven.cs.distrinet.chameleon.util.Util;
import be.kuleuven.cs.distrinet.chameleon.util.association.Multi;

public class SimpleNameDeclarationWithParametersSignature extends DeclarationWithParametersSignature {

  public SimpleNameDeclarationWithParametersSignature(String name) {
    setName(name);
  }
  
  public String name() {
    return _name;
  }
  
  public void setName(String name) {
    _name = name;
    // Robustness check needed to deal with partially parsed code.
    if(_name != null) {
    	_nameHash = _name.hashCode();
    }
  }
  
  public int nameHash() {
  	return _nameHash;
  }
  
  private int _nameHash;
  
  private String _name;

///*********************
//* FORMAL PARAMETERS *
//*********************/
//
  public List<TypeReference> typeReferences() {
    return _parameterTypes.getOtherEnds();
  }

  public void add(TypeReference arg) {
    add(_parameterTypes,arg);
  }

  public void addAll(List<TypeReference> trefs) {
  	for(TypeReference tref:trefs) {
  		add(tref);
  	}
  }
  
  public void remove(TypeReference arg) {
    remove(_parameterTypes,arg);
   }

  public int nbTypeReferences() {
   return _parameterTypes.size();
  }  

  private Multi<TypeReference> _parameterTypes = new Multi<TypeReference>(this,"parameter types");
  {
  	_parameterTypes.enableCache();
  }

  
  @Override
  protected SimpleNameDeclarationWithParametersSignature cloneSelf() {
  	return new SimpleNameDeclarationWithParametersSignature(name());
  }

  @Override
	public boolean uniSameAs(Element other) throws LookupException {
		boolean result = false;
		if(other instanceof SimpleNameDeclarationWithParametersSignature) {
			SimpleNameDeclarationWithParametersSignature sig = (SimpleNameDeclarationWithParametersSignature) other;
			result = name().equals(sig.name()) && sameParameterTypesAs(sig);
		}
		return result;
	}
  
	@Override
	public List<Type> parameterTypes() throws LookupException {
		List<Type> result = Lists.create(_parameterTypes.size());
  	for(TypeReference ref: typeReferences()) {
  		result.add(ref.getType());
  	}
		return result;
	}

	@Override
	public Verification verifySelf() {
		Verification result = Valid.create();
		if(name() == null) {
			result = result.and(new BasicProblem(this, "The signature has no name."));
		}
		return result;
	}

  @Override
  public String toString() {
  	StringBuffer result = new StringBuffer();
  	result.append(name());
  	result.append("(");
  	List<TypeReference> types = typeReferences();
  	int size = types.size();
		if(size > 0) {
  		result.append(types.get(0).toString());
  	}
  	for(int i = 1; i < size; i++) {
  		result.append(",");
  		result.append(types.get(i).toString());
  	}
  	result.append(")");
  	return result.toString();
  }

	@Override
	public Signature lastSignature() {
		return this;
	}
	
	@Override
	public List<Signature> signatures() {
		return Util.createSingletonList((Signature)this);
	}

	@Override
	public int length() {
		return 1;
	}

	@Override
	public int nbFormalParameters() {
		return _parameterTypes.size();
	}

}
