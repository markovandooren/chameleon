package chameleon.core.variable;

import org.rejuse.association.SingleAssociation;

import chameleon.core.declaration.MissingSignature;
import chameleon.core.declaration.Signature;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespace.NamespaceElementImpl;
import chameleon.core.type.Type;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.exception.ChameleonProgrammerException;

public abstract class VariableImpl<E extends VariableImpl<E,P,F>, P extends Element, F extends Variable> 
       extends NamespaceElementImpl<E, Element>
       implements Variable<E,Element,F> {
	
	public VariableImpl(SimpleNameSignature signature) {
		setSignature(signature);
	}
	
	public Type declarationType() throws LookupException {
		return getType();
	}

  public void setSignature(Signature signature) {
  	if(signature instanceof SimpleNameSignature) {
  		if(signature != null) {
  			_signature.connectTo(signature.parentLink());
  		} else {
  			_signature.connectTo(null);
  		}
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
  
  private SingleAssociation<VariableImpl, SimpleNameSignature> _signature = new SingleAssociation<VariableImpl, SimpleNameSignature>(this);

	@Override
	public VerificationResult verifySelf() {
		if(signature() != null) {
		  return Valid.create();
		} else {
			return new MissingSignature(this); 
		}
	}

}
