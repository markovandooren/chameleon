package chameleon.core.type;

import java.util.ArrayList;
import java.util.List;

import chameleon.core.declaration.Signature;
import chameleon.core.element.Element;

public class TypeSignature extends Signature<TypeSignature, Type>{

	public TypeSignature(String name) {
		setName(name);
	}
	
	private String _name;
	
	public String getName() {
		return _name;
	}
	
	public void setName(String name) {
		_name = name;
	}
	
	@Override
	public TypeSignature clone() {
	  return new TypeSignature(getName());
	}

	//@FIXME change when type parameters are added?
	public List<? extends Element> getChildren() {
	  return new ArrayList();
	}

	public boolean sameAs(Object other) {
		boolean result = false;
		if(other instanceof TypeSignature) {
			TypeSignature sig = (TypeSignature)other;
			result = sig.getName().equals(getName());
		}
		return result;
	}
}
