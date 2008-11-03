package chameleon.core.variable;

import org.rejuse.association.Reference;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.type.TypeReference;

public abstract class RegularVariable<E extends RegularVariable<E,P>, P extends VariableContainer> extends Variable<E,P> {

	public RegularVariable(SimpleNameSignature sig, TypeReference typeRef) {
		super(sig);
    setTypeReference(typeRef);
	}

	/**
	 * TYPE
	 */
	private Reference<Variable,TypeReference> _typeReference = new Reference<Variable,TypeReference>(this);


  public TypeReference getTypeReference() {
    return _typeReference.getOtherEnd();
  }

  public void setTypeReference(TypeReference type) {
    _typeReference.connectTo(type.getParentLink());
  }


}
