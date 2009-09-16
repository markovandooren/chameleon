package chameleon.core.namespacepart;

import java.util.ArrayList;
import java.util.List;

import org.rejuse.association.SingleAssociation;

import chameleon.core.declaration.Declaration;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespace.Namespace;
import chameleon.core.namespace.NamespaceOrType;
import chameleon.core.reference.ElementReference;
import chameleon.util.Util;

/**
 * @author Marko van Dooren
 */
public class DemandImport extends Import<DemandImport> {
  
  public DemandImport(ElementReference<?, ?, ? extends Namespace> ref) {
    setNamespaceReference( (ElementReference<ElementReference<?, ? super DemandImport, ? extends Namespace>, ? super DemandImport, ? extends Namespace>) ref);
  }

  
  public List children() {
    return Util.createNonNullList(namespaceReference());
  }

  
	private SingleAssociation<DemandImport,ElementReference<?, ?, ? extends Namespace>> _packageOrType = new SingleAssociation<DemandImport,ElementReference<?, ?, ? extends Namespace>>(this);

  
  public ElementReference<?, ?, ? extends Namespace> namespaceReference() {
    return _packageOrType.getOtherEnd();
  }
  
  public void setNamespaceReference(ElementReference<ElementReference<?, ? super DemandImport, ? extends Namespace>, ? super DemandImport, ? extends Namespace> ref) {
  	if(ref != null) {
  		_packageOrType.connectTo(ref.parentLink());
  	}
  	else {
  		_packageOrType.connectTo(null);
  	}
  }
  
  public NamespaceOrType declarationContainer() throws LookupException {
    return namespaceReference().getElement();
  }
  
  @Override
  public DemandImport clone() {
    return new DemandImport(namespaceReference().clone());
  }


	@Override
	public List<Declaration> demandImports() throws LookupException {
		return declarationContainer().declarations();
	}


	@Override
	public List<Declaration> directImports() throws LookupException {
		return new ArrayList<Declaration>();
	}


	@Override
	public <D extends Declaration> List<D> demandImports(DeclarationSelector<D> selector) throws LookupException {
		D selected = declarationContainer().localStrategy().lookUp(selector);
		List<D> result = new ArrayList<D>();
		Util.addNonNull(selected, result);
		return result;
		//return declarationContainer().declarations(selector);
	}


	@Override
	public <D extends Declaration> List<D> directImports(DeclarationSelector<D> selector) throws LookupException {
		return new ArrayList<D>();
	}
  


}
