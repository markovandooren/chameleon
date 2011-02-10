package chameleon.core.declaration;

import java.util.ArrayList;
import java.util.List;

import org.rejuse.association.OrderedMultiAssociation;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespace.NamespaceElement;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeReference;
import chameleon.util.Util;

public class SimpleNameDeclarationWithParametersSignature extends DeclarationWithParametersSignature<SimpleNameDeclarationWithParametersSignature>{

  public SimpleNameDeclarationWithParametersSignature(String name) {
    setName(name);
  }
  
  public String name() {
    return _name;
  }
  
  public void setName(String name) {
    _name = name;
    _nameHash = _name.hashCode();
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
   _parameterTypes.add(arg.parentLink());
  }

  public void remove(TypeReference arg) {
    _parameterTypes.remove(arg.parentLink());
   }

  public int nbTypeReferences() {
   return _parameterTypes.size();
  }  

  private OrderedMultiAssociation<SimpleNameDeclarationWithParametersSignature,TypeReference> _parameterTypes = new OrderedMultiAssociation<SimpleNameDeclarationWithParametersSignature,TypeReference>(this);

  
  @Override
  public SimpleNameDeclarationWithParametersSignature clone() {
  	SimpleNameDeclarationWithParametersSignature result = new SimpleNameDeclarationWithParametersSignature(name());
  	for(TypeReference ref: typeReferences()) {
  		result.add(ref.clone());
  	}
  	return result;
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
		List<Type> result = new ArrayList<Type>();
  	for(TypeReference ref: typeReferences()) {
  		result.add(ref.getType());
  	}
		return result;
	}

	public List<? extends Element> children() {
		return typeReferences();
	}

	@Override
	public VerificationResult verifySelf() {
		VerificationResult result = Valid.create();
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
