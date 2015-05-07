package org.aikodi.chameleon.core.lookup;

import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.DeclarationContainer;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;

/**
 * A selector that selects the declarator of the declaration selected by
 * the decorated selector.
 * 
 * @author Marko van Dooren
 */
public class DeclaratorSelector implements DeclarationSelector<Declaration>{
	
	public DeclaratorSelector(DeclarationSelector selector) {
		if(selector == null) {
			throw new ChameleonProgrammerException("The wrapped selector of a declarator selector cannot be null.");
		}
		_selector = selector;
	}
	
  private DeclarationSelector _selector;

  @Override
public List selection(List selectionCandidates) throws LookupException {
  	return _selector.declarators(selectionCandidates);
  }

	@Override
	public boolean canSelect(Class<? extends Declaration> type) {
		return _selector.canSelect(type);
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
