package be.kuleuven.cs.distrinet.chameleon.oo.namespacedeclaration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.namespacedeclaration.Import;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

/**
 * @author Marko van Dooren
 */
public class TypeImport extends Import {

  public TypeImport(TypeReference ref) {
    setTypeReference(ref);
  }
  
	private Single<TypeReference> _typeReference = new Single<TypeReference>(this);

  public TypeReference getTypeReference() {
    return (TypeReference)_typeReference.getOtherEnd();
  }

  public void setTypeReference(TypeReference reference) {
  	set(_typeReference,reference);
  }
  
  public Type type() throws LookupException {
  	Type result = getTypeReference().getType().baseType();
  	if(result != null) {
      return result;
  	} else {
  		throw new LookupException("Lookup of type import reference returns null",getTypeReference());
  	}
  }

  @Override
  public TypeImport clone() {
    return new TypeImport(getTypeReference().clone());
  }

	@Override
	public List<Declaration> demandImports() {
		return new ArrayList<Declaration>();
	}

	@Override
	public List<Declaration> directImports() throws LookupException {
		//lookupLogger().debug("NamespacePart of " + nearestAncestor(NamespacePart.class).getFullyQualifiedName()+"Looking up direct import: "+getTypeReference().signature());
		List<Declaration> result = new ArrayList<Declaration>();
		result.add(type());
		return result;
	}

	@Override
	public <D extends Declaration> List<D> demandImports(DeclarationSelector<D> selector) throws LookupException {
		return Collections.EMPTY_LIST;
	}

	@Override
	public <D extends Declaration> List<D> directImports(DeclarationSelector<D> selector) throws LookupException {
//		lookupLogger().debug("NamespacePart of " + nearestAncestor(NamespacePart.class).getFullyQualifiedName()+"Looking up direct import: "+getTypeReference().signature());
		List<Declaration> tmp = new ArrayList<Declaration>();
		tmp.add(type());
		return selector.selection(tmp);
	}

}
