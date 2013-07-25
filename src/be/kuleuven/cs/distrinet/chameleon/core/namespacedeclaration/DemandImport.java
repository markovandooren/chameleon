package be.kuleuven.cs.distrinet.chameleon.core.namespacedeclaration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.ImmutableList;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.DeclarationContainer;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationCollector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.SelectionResult;
import be.kuleuven.cs.distrinet.chameleon.core.reference.SimpleReference;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.test.CrossReferenceTest;
import be.kuleuven.cs.distrinet.chameleon.util.Util;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

/**
 * @author Marko van Dooren
 */
public class DemandImport extends Import {
  
  public DemandImport(SimpleReference<? extends DeclarationContainer> ref) {
  	setContainerReference(ref);
  }
  
	private Single<SimpleReference<? extends DeclarationContainer>> _packageOrType = new Single<SimpleReference<? extends DeclarationContainer>>(this);

  
  public SimpleReference<? extends DeclarationContainer> containerReference() {
    return _packageOrType.getOtherEnd();
  }
  
  public void setContainerReference(SimpleReference<? extends DeclarationContainer> ref) {
  	set(_packageOrType,ref);
  }
  
  public DeclarationContainer declarationContainer() throws LookupException {
    return containerReference().getElement();
  }
  
  @Override
  protected DemandImport cloneSelf() {
    return new DemandImport(null);
  }


	@Override
	public List<Declaration> demandImports() throws LookupException {
		return (List<Declaration>) declarationContainer().declarations();
	}

	protected void filterImportedElements(List<Declaration> declarations ) throws LookupException {
	}
	
	@Override
	public List<Declaration> directImports() throws LookupException {
		return Collections.EMPTY_LIST;
	}

	@Override
	public <D extends Declaration> List<? extends SelectionResult> demandImports(DeclarationSelector<D> selector) throws LookupException {
			return importedDeclarations(selector);
	}

	private <D extends Declaration> List<? extends SelectionResult> importedDeclarations(DeclarationSelector<D> selector) throws LookupException {
		DeclarationCollector collector = new DeclarationCollector(selector);
		declarationContainer().localContext().lookUp(collector);
		if(! collector.willProceed()) { 
		  return ImmutableList.of((D) collector.result());
		} else {
			return Collections.EMPTY_LIST;
		}
	}
	
	@Override
	public <D extends Declaration> List<? extends SelectionResult> directImports(DeclarationSelector<D> selector) throws LookupException {
		return Collections.EMPTY_LIST;
	}

	@Override
	public Verification verifySelf() {
		return Valid.create();
	}
	
	public boolean importsSameAs(Import other) throws LookupException {
		return super.importsSameAs(other);
	}
	
	@Override
	public String toString() {
		return "demand import of " + containerReference() == null ? null : containerReference().toString();
	}
}
