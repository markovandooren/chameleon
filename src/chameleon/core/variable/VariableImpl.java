package chameleon.core.variable;

import org.rejuse.association.Reference;

import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.namespacepart.NamespaceElementImpl;

public abstract class VariableImpl<E extends VariableImpl<E,P>, P extends DeclarationContainer> 
       extends NamespaceElementImpl<E, P>
       implements Variable<E,P> {
	
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
  
  private Reference<VariableImpl, SimpleNameSignature> _signature = new Reference<VariableImpl, SimpleNameSignature>(this);

}
