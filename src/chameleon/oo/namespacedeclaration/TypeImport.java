package chameleon.oo.namespacedeclaration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import chameleon.core.declaration.Declaration;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespacedeclaration.Import;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeReference;
import chameleon.util.association.Single;

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
