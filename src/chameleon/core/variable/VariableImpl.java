package chameleon.core.variable;

import org.rejuse.association.SingleAssociation;

import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.declaration.MissingSignature;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.namespace.NamespaceElementImpl;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;

public abstract class VariableImpl<E extends VariableImpl<E,P,F>, P extends DeclarationContainer, F extends Variable> 
       extends NamespaceElementImpl<E, P>
       implements Variable<E,P,F> {
	
	public VariableImpl(SimpleNameSignature signature) {
		setSignature(signature);
	}

  public void setSignature(SimpleNameSignature signature) {
    if(signature != null) {
      _signature.connectTo(signature.parentLink());
    } else {
      _signature.connectTo(null);
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
