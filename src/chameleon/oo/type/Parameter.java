package chameleon.oo.type;

import org.rejuse.association.SingleAssociation;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.Signature;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.ElementImpl;
import chameleon.core.lookup.LookupException;
import chameleon.exception.ChameleonProgrammerException;

public abstract class Parameter extends ElementImpl implements Declaration {
	
	public Parameter(SimpleNameSignature sig) {
		setSignature(sig);
	}
	
	public abstract Declaration selectionDeclaration() throws LookupException;
	
	public abstract Parameter clone();
	
	public void setName(String name) {
		setSignature(new SimpleNameSignature(name));
	}
	
	public void setSignature(Signature signature) {
  	if(signature instanceof SimpleNameSignature || signature == null) {
  		setAsParent(_signature,(SimpleNameSignature)signature);
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
  
  private SingleAssociation<Parameter, SimpleNameSignature> _signature = new SingleAssociation<Parameter, SimpleNameSignature>(this);

}
