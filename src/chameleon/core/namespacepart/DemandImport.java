package chameleon.core.namespacepart;

import java.util.ArrayList;
import java.util.List;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.lookup.DeclarationCollector;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespace.Namespace;
import chameleon.core.reference.ElementReference;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.util.Util;
import chameleon.util.association.Single;

/**
 * @author Marko van Dooren
 */
public class DemandImport extends Import {
  
  public DemandImport(ElementReference<? extends Namespace> ref) {
    setNamespaceReference( (ElementReference<? extends Namespace>) ref);
  }
  
	private Single<ElementReference<? extends Namespace>> _packageOrType = new Single<ElementReference<? extends Namespace>>(this);

  
  public ElementReference<? extends Namespace> namespaceReference() {
    return _packageOrType.getOtherEnd();
  }
  
  public void setNamespaceReference(ElementReference<? extends Namespace> ref) {
  	set(_packageOrType,ref);
  }
  
  public DeclarationContainer declarationContainer() throws LookupException {
    return namespaceReference().getElement();
  }
  
  @Override
  public DemandImport clone() {
    return new DemandImport((ElementReference<? extends Namespace>) namespaceReference().clone());
  }


	@Override
	public List<Declaration> demandImports() throws LookupException {
		return (List<Declaration>) declarationContainer().declarations();
	}


	@Override
	public List<Declaration> directImports() throws LookupException {
		return new ArrayList<Declaration>();
	}


	@Override
	public <D extends Declaration> List<D> demandImports(DeclarationSelector<D> selector) throws LookupException {
		DeclarationCollector<D> collector = new DeclarationCollector<D>(selector);
		declarationContainer().localStrategy().lookUp(collector);
		List<D> result = new ArrayList<D>();
		if(! collector.willProceed()) { 
		  D selected = collector.result();
		  Util.addNonNull(selected, result);
		}
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
