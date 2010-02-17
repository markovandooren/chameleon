package chameleon.core.declaration;

import java.util.ArrayList;
import java.util.List;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.util.Util;

/**
 * A class of signatures that consist of a simple name.
 * 
 * @author Marko van Dooren
 */
public class SimpleNameSignature extends Signature<SimpleNameSignature, Element>{

  public SimpleNameSignature(String name) {
    setName(name);
  }
  
  public String name() {
    return _name;
  }
  
  public void setName(String name) {
    _name = name;
  }
  
  private String _name;

  @Override
	public boolean uniSameAs(Element other) throws LookupException {
		boolean result = false;
		if(other instanceof SimpleNameSignature) {
			SimpleNameSignature sig = (SimpleNameSignature) other;
			result = name().equals(sig.name());
		}
		return result;
	}

	@Override
	public SimpleNameSignature clone() {
    return new SimpleNameSignature(name());
	}

	public List<? extends Element> children() {
		return new ArrayList<Element>();
	}

	@Override
	public VerificationResult verifySelf() {
		if(_name == null) {
			return new SignatureWithoutName(this);
		} else {
			return Valid.create();
		}
	}
	
	@Override
	public String toString() {
		return _name;
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

}
