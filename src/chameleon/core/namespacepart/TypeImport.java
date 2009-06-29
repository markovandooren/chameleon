package chameleon.core.namespacepart;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rejuse.association.Reference;

import chameleon.core.MetamodelException;
import chameleon.core.context.LookupException;
import chameleon.core.declaration.Declaration;
import chameleon.core.element.Element;
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
	public Set<Declaration> demandImports() {
		return new HashSet<Declaration>();
	}

	@Override
	public Set<Declaration> directImports() throws LookupException {
		Set<Declaration> result = new HashSet<Declaration>();
		result.add(type());
		return result;
	}
  
	
	
	

	  
  
}
