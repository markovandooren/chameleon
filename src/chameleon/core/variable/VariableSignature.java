package chameleon.core.variable;

import java.util.ArrayList;
import java.util.List;

import chameleon.core.declaration.Signature;
import chameleon.core.element.Element;

public class VariableSignature extends Signature<VariableSignature, Variable>{
  
  public VariableSignature(String name) {
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
  public VariableSignature clone() {
    return new VariableSignature(getName());
  }

  public List<? extends Element> getChildren() {
    return new ArrayList<Element>();
  }
  
  public boolean sameAs(Object other) {
  	boolean result = false;
  	if(other instanceof VariableSignature) {
  		VariableSignature sig = (VariableSignature) other;
  		if(sig.getName().equals(getName())) {
  			result = true;
  		}
  	}
  	return result;
  }

}
