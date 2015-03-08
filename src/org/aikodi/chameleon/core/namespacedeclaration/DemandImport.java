package org.aikodi.chameleon.core.namespacedeclaration;

import java.util.Collections;
import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.DeclarationContainer;
import org.aikodi.chameleon.core.lookup.DeclarationCollector;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.SelectionResult;
import org.aikodi.chameleon.core.reference.NameReference;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.util.association.Single;

import com.google.common.collect.ImmutableList;

/**
 * @author Marko van Dooren
 */
public class DemandImport extends Import {
  
  public DemandImport(NameReference<? extends DeclarationContainer> ref) {
  	setContainerReference(ref);
  }
  
	private Single<NameReference<? extends DeclarationContainer>> _packageOrType = new Single<NameReference<? extends DeclarationContainer>>(this);

  
  public NameReference<? extends DeclarationContainer> containerReference() {
    return _packageOrType.getOtherEnd();
  }
  
  public void setContainerReference(NameReference<? extends DeclarationContainer> ref) {
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
	
	@Override
   public boolean importsSameAs(Import other) throws LookupException {
		return super.importsSameAs(other);
	}
	
	@Override
	public String toString() {
		return "demand import of " + containerReference() == null ? null : containerReference().toString();
	}
}
