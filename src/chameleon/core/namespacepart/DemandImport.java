package chameleon.core.namespacepart;

import java.util.List;

import org.rejuse.association.Reference;

import chameleon.core.MetamodelException;
import chameleon.core.namespace.NamespaceOrType;
import chameleon.core.namespace.NamespaceOrTypeReference;
import chameleon.core.type.Type;
import chameleon.util.Util;

/**
 * @author Marko van Dooren
 */
public class DemandImport extends Import<DemandImport> {
  
  public DemandImport(NamespaceOrTypeReference ref) {
    setNamespaceOrTypeReference(ref);
  }

  
  public List getChildren() {
    return Util.createNonNullList(getNamespaceOrTypeReference());
  }

  
	private Reference<DemandImport,NamespaceOrTypeReference> _packageOrType = new Reference<DemandImport,NamespaceOrTypeReference>(this);

  
  public NamespaceOrTypeReference getNamespaceOrTypeReference() {
    return _packageOrType.getOtherEnd();
  }
  
  public void setNamespaceOrTypeReference(NamespaceOrTypeReference ref) {
  	if(ref != null) {
  		_packageOrType.connectTo(ref.getParentLink());
  	}
  	else {
  		_packageOrType.connectTo(null);
  	}
  }
  
  public NamespaceOrType getElement() throws MetamodelException {
    return getNamespaceOrTypeReference().getNamespaceOrType();
  }
  
//  public Type getType(String name) throws MetamodelException {
//    NamespaceOrType self = getElement();
//    if(self != null) {
//      return self.getType(name);
//    }
//    else {
//      getElement();
//      throw new MetamodelException();
//    }
//  }


  @Override
  public DemandImport clone() {
    return new DemandImport(getNamespaceOrTypeReference().clone());
  }
  


}
