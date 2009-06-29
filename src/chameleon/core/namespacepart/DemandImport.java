package chameleon.core.namespacepart;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rejuse.association.Reference;

import chameleon.core.MetamodelException;
import chameleon.core.context.LookupException;
import chameleon.core.declaration.Declaration;
import chameleon.core.namespace.NamespaceOrType;
import chameleon.core.namespace.NamespaceOrTypeReference;
import chameleon.util.Util;

/**
 * @author Marko van Dooren
 */
public class DemandImport extends Import<DemandImport> {
  
  public DemandImport(NamespaceOrTypeReference ref) {
    setNamespaceOrTypeReference(ref);
  }

  
  public List children() {
    return Util.createNonNullList(getNamespaceOrTypeReference());
  }

  
	private Reference<DemandImport,NamespaceOrTypeReference> _packageOrType = new Reference<DemandImport,NamespaceOrTypeReference>(this);

  
  public NamespaceOrTypeReference getNamespaceOrTypeReference() {
    return _packageOrType.getOtherEnd();
  }
  
  public void setNamespaceOrTypeReference(NamespaceOrTypeReference ref) {
  	if(ref != null) {
  		_packageOrType.connectTo(ref.parentLink());
  	}
  	else {
  		_packageOrType.connectTo(null);
  	}
  }
  
  public NamespaceOrType declarationContainer() throws LookupException {
    return getNamespaceOrTypeReference().getNamespaceOrType();
  }
  
  @Override
  public DemandImport clone() {
    return new DemandImport(getNamespaceOrTypeReference().clone());
  }


	@Override
	public Set<Declaration> demandImports() throws LookupException {
		return declarationContainer().declarations();
	}


	@Override
	public Set<Declaration> directImports() throws LookupException {
		return new HashSet<Declaration>();
	}
  


}
