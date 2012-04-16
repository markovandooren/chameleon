package chameleon.oo.variable;

import java.util.ArrayList;
import java.util.List;

import org.rejuse.property.Property;
import org.rejuse.property.PropertyMutex;

import chameleon.core.declaration.MissingSignature;
import chameleon.core.declaration.Signature;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.ElementImpl;
import chameleon.core.lookup.LookupException;
import chameleon.core.modifier.Modifier;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.exception.ModelException;
import chameleon.oo.type.Type;
import chameleon.util.association.Single;

public abstract class VariableImpl extends ElementImpl implements Variable {
	
	public abstract VariableImpl clone();

	public VariableImpl(SimpleNameSignature signature) {
		setSignature(signature);
	}
	
	public Type declarationType() throws LookupException {
		return getType();
	}
	
	public boolean complete() {
		return true;
	}
	
	public void setName(String name) {
		signature().setName(name);
	}

  public void setSignature(Signature signature) {
  	if(signature instanceof SimpleNameSignature || signature == null) {
  		set(_signature, (SimpleNameSignature)signature);
  	} else {
  		throw new ChameleonProgrammerException("Setting wrong type of signature. Provided: "+(signature == null ? null :signature.getClass().getName())+" Expected SimpleNameSignature");
  	}
  }
  
  /**
   * Return the signature of this member.
   */
  public SimpleNameSignature signature() {
    return _signature.getOtherEnd();
  }
  
	@Override
	public String name() {
		return signature().name();
	}

	private Single<SimpleNameSignature> _signature = new Single<SimpleNameSignature>(this);

	@Override
	public VerificationResult verifySelf() {
		if(signature() != null) {
		  return Valid.create();
		} else {
			return new MissingSignature(this); 
		}
	}

	/**
	 * COPIED FROM TypeElementImpl
	 */
  public List<Modifier> modifiers(PropertyMutex mutex) throws ModelException {
  	Property property = property(mutex);
  	List<Modifier> result = new ArrayList<Modifier>();
  	for(Modifier mod: modifiers()) {
  		if(mod.impliesTrue(property)) {
  			result.add(mod);
  		}
  	}
  	return result;
  }

	public List<Modifier> modifiers(Property property) throws ModelException {
		List<Modifier> result = new ArrayList<Modifier>();
		for(Modifier modifier: modifiers()) {
			if(modifier.impliesTrue(property)) {
				result.add(modifier);
			}
		}
		return result;
	}
	
	public String toString() {
		return getTypeReference().toString() +" "+signature().toString();
	}
}
