package chameleon.core.declaration;

import java.util.ArrayList;
import java.util.List;

import chameleon.core.context.LookupException;
import chameleon.core.element.Element;

public class SimpleNameSignature extends Signature<SimpleNameSignature, Element>{

  public SimpleNameSignature(String name) {
    setName(name);
  }
  
  public String getName() {
    return _name;
  }
  
  public void setName(String name) {
    _name = name;
  }
  
  private String _name;

	public boolean sameAs(Signature other) throws LookupException {
		boolean result = false;
		if(other instanceof SimpleNameSignature) {
			SimpleNameSignature sig = (SimpleNameSignature) other;
			result = getName().equals(sig.getName());
		}
		return result;
	}

	@Override
	public SimpleNameSignature clone() {
    return new SimpleNameSignature(getName());
	}

	public List<? extends Element> children() {
		return new ArrayList<Element>();
	}

}
