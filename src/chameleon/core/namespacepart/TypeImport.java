package chameleon.core.namespacepart;

import java.util.List;

import org.rejuse.association.Reference;

import chameleon.core.MetamodelException;
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
  
  public List<Element> getChildren() {
    return Util.createNonNullList(getTypeReference());
  }

	private Reference<TypeImport, TypeReference> _typeReference = new Reference<TypeImport, TypeReference>(this);

  
  public TypeReference getTypeReference() {
    return (TypeReference)_typeReference.getOtherEnd();
  }

  public void setTypeReference(TypeReference reference) {
  	if(reference != null) {
  		_typeReference.connectTo(reference.getParentLink());
  	}
  	else {
  		_typeReference.connectTo(null);
  	}
  }
  
  public Type getType() throws MetamodelException {
  	Type result = getTypeReference().getType();
  	if(result != null) {
      return result;
  	} else {
  		getTypeReference().getType();
  		throw new MetamodelException();
  	}
  }

  @Override
  public TypeImport clone() {
    return new TypeImport(getTypeReference().clone());
  }
  
	
	
	

	  
  
}
