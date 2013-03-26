package be.kuleuven.cs.distrinet.chameleon.core.lookup;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.DeclarationContainer;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Signature;
import be.kuleuven.cs.distrinet.chameleon.core.relation.WeakPartialOrder;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;

public class DeclaratorSelector extends DeclarationSelector<Declaration>{
	
	public DeclaratorSelector(DeclarationSelector selector) {
		if(selector == null) {
			throw new ChameleonProgrammerException("The wrapped selector of a declarator selector cannot be null.");
		}
		_selector = selector;
	}
	
//	@Override
//  public Declaration actualDeclaration(Declaration declarator) throws LookupException {
//  	return declarator;
//  }

  private DeclarationSelector _selector;

  public List selection(List selectionCandidates) throws LookupException {
  	return _selector.declarators(selectionCandidates);
  }
  
	@Override
	public WeakPartialOrder order() {
		return _selector.order();
	}

	@Override
	public Class selectedClass() {
		return _selector.selectedClass();
	}

	@Override
	public String selectionName(DeclarationContainer container) throws LookupException {
		return _selector.selectionName(container);
	}

	@Override
	public List declarators(List<? extends Declaration> selectionCandidates) throws LookupException {
		return _selector.declarators(selectionCandidates);
	}
	
	@Override
	public boolean isGreedy() {
		return false;
	}

}
