package chameleon.core.namespacepart;

import java.util.ArrayList;
import java.util.List;

import org.rejuse.association.SingleAssociation;

import chameleon.core.declaration.Declaration;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespace.Namespace;
import chameleon.core.namespace.NamespaceOrType;
import chameleon.core.reference.ElementReference;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.util.Util;

/**
 * @author Marko van Dooren
 */
public class DemandImport extends Import<DemandImport> {
  
  public DemandImport(ElementReference<?, ?, ? extends Namespace> ref) {
    setNamespaceReference( (ElementReference<ElementReference<?, ?, ? extends Namespace>, ?, ? extends Namespace>) ref);
  }

  
  public List<Element> children() {
    return Util.createNonNullList(namespaceReference());
  }

  
	private SingleAssociation<DemandImport,ElementReference<?, ?, ? extends Namespace>> _packageOrType = new SingleAssociation<DemandImport,ElementReference<?, ?, ? extends Namespace>>(this);

  
  public ElementReference<?, ?, ? extends Namespace> namespaceReference() {
    return _packageOrType.getOtherEnd();
  }
  
  public void setNamespaceReference(ElementReference<ElementReference<?, ?, ? extends Namespace>, ?, ? extends Namespace> ref) {
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
    return new DemandImport((ElementReference<?, ?, ? extends Namespace>) namespaceReference().clone());
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
	}


	@Override
	public <D extends Declaration> List<D> directImports(DeclarationSelector<D> selector) throws LookupException {
		return new ArrayList<D>();
	}


	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}
	
	public Namespace importedNamespace() throws LookupException {
		return namespaceReference().getElement();
	}

	public boolean importsSameAs(Import other) throws LookupException {
		return super.importsSameAs(other);
	}
//	@Override
//	public boolean importsSameAs(Import other) throws LookupException {
//		return other instanceof DemandImport && ((DemandImport)other).importedNamespace().sameAs(importedNamespace());
//	}
//  
}
