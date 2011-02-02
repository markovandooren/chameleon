package chameleon.oo.type;

import org.rejuse.association.SingleAssociation;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.Signature;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespace.NamespaceElementImpl;
import chameleon.exception.ChameleonProgrammerException;

public abstract class Parameter<E extends Parameter<E,T>, T extends Declaration> extends NamespaceElementImpl<E> implements Declaration<E,SimpleNameSignature,T> {
	
	public Parameter(SimpleNameSignature sig) {
		setSignature(sig);
	}
	
	public abstract T selectionDeclaration() throws LookupException;
	
	public abstract E clone();
	
	public void setName(String name) {
		setSignature(new SimpleNameSignature(name));
	}
	
	public void setSignature(Signature signature) {
  	if(signature instanceof SimpleNameSignature) {
  		_signature.connectTo(signature.parentLink());
  	} else if(signature == null) {
  		_signature.connectTo(null);
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
