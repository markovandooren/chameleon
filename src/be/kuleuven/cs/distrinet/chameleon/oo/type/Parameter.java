package be.kuleuven.cs.distrinet.chameleon.oo.type;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Signature;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.SimpleNameSignature;
import be.kuleuven.cs.distrinet.chameleon.core.element.ElementImpl;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

public abstract class Parameter extends ElementImpl implements Declaration {
	
	public Parameter(SimpleNameSignature sig) {
		setSignature(sig);
	}
	
	public abstract Declaration selectionDeclaration() throws LookupException;
	
	public void setName(String name) {
		setSignature(new SimpleNameSignature(name));
	}
	
	public void setSignature(Signature signature) {
  	if(signature instanceof SimpleNameSignature || signature == null) {
  		set(_signature,(SimpleNameSignature)signature);
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

	private Single<SimpleNameSignature> _signature = new Single<SimpleNameSignature>(this,true);

}
