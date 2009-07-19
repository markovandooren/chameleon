package chameleon.core.type.generics;

import org.rejuse.association.Reference;

import chameleon.core.MetamodelException;
import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespacepart.NamespaceElementImpl;
import chameleon.core.scope.LexicalScope;
import chameleon.core.scope.Scope;
import chameleon.core.type.Type;

public abstract class GenericParameter<E extends GenericParameter<E>> extends NamespaceElementImpl<E, DeclarationContainer> implements Declaration<E,DeclarationContainer,SimpleNameSignature,E>{
	
	public GenericParameter(SimpleNameSignature signature) {
		setSignature(signature);
	}

	public abstract E clone();
	
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
  
  private Reference<GenericParameter, SimpleNameSignature> _signature = new Reference<GenericParameter, SimpleNameSignature>(this);

  public Declaration resolveForMatch() throws LookupException {
  	return this;
  }
  
  public E resolveForResult() throws LookupException {
  	return (E) this;
  }


	public abstract boolean compatibleWith(GenericParameter other) throws LookupException;
	
	public Scope scope() throws MetamodelException {
		return new LexicalScope(nearestAncestor(Type.class));
	}


}
