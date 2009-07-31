package chameleon.core.namespacepart;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rejuse.association.Reference;

import chameleon.core.declaration.Declaration;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.type.Type;
import chameleon.core.type.TypeReference;
import chameleon.util.Util;

/**
 * @author Marko van Dooren
 */
public class TypeImport extends Import<TypeImport> {

  public TypeImport(TypeReference ref) {
    setTypeReference(ref);
  }
  
  public List<Element> children() {
    return Util.createNonNullList(getTypeReference());
  }

	private Reference<TypeImport, TypeReference> _typeReference = new Reference<TypeImport, TypeReference>(this);

  
  public TypeReference getTypeReference() {
    return (TypeReference)_typeReference.getOtherEnd();
  }

  public void setTypeReference(TypeReference reference) {
  	if(reference != null) {
  		_typeReference.connectTo(reference.parentLink());
  	}
  	else {
  		_typeReference.connectTo(null);
  	}
  }
  
  public Type type() throws LookupException {
  	Type result = getTypeReference().getType();
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
		lookupLogger().debug("NamespacePart of " + nearestAncestor(NamespacePart.class).getFullyQualifiedName()+"Looking up direct import: "+getTypeReference().getName());
		List<Declaration> result = new ArrayList<Declaration>();
		result.add(type());
		return result;
	}

	@Override
	public <D extends Declaration> List<D> demandImports(DeclarationSelector<D> selector) throws LookupException {
		return new ArrayList<D>();
	}

	@Override
	public <D extends Declaration> List<D> directImports(DeclarationSelector<D> selector) throws LookupException {
		lookupLogger().debug("NamespacePart of " + nearestAncestor(NamespacePart.class).getFullyQualifiedName()+"Looking up direct import: "+getTypeReference().getName());
		List<D> result = new ArrayList<D>();
		D element = selector.selection(type());
		if(element != null) {
		  result.add(element);
		}
		return result;
	}
  
	
	
	

	  
  
}
