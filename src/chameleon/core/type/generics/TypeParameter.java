package chameleon.core.type.generics;

import org.rejuse.association.Reference;

import chameleon.core.MetamodelException;
import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.ChameleonProgrammerException;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespacepart.NamespaceElementImpl;
import chameleon.core.scope.LexicalScope;
import chameleon.core.scope.Scope;
import chameleon.core.type.Type;

public abstract class TypeParameter<E extends TypeParameter<E>> extends NamespaceElementImpl<E, TypeParameterBlock> implements Declaration<E,TypeParameterBlock,SimpleNameSignature,Type>{
	
	public TypeParameter(SimpleNameSignature signature) {
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
  
  private Reference<TypeParameter, SimpleNameSignature> _signature = new Reference<TypeParameter, SimpleNameSignature>(this);

  public Type actualDeclaration() throws LookupException {
  	throw new ChameleonProgrammerException();
  }


	public final boolean compatibleWith(TypeParameter other) throws LookupException {
		return upperBound().subTypeOf(other.upperBound()) && other.lowerBound().subTypeOf(lowerBound());
	}
	
	public abstract Type upperBound() throws LookupException;
	
	public abstract Type lowerBound() throws LookupException;

	public Scope scope() throws MetamodelException {
		return new LexicalScope(nearestAncestor(Type.class));
	}

	public abstract Declaration resolveForRoundTrip() throws LookupException;

}
