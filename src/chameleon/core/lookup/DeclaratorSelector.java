package chameleon.core.lookup;

import java.util.ArrayList;
import java.util.List;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.Signature;
import chameleon.core.relation.WeakPartialOrder;
import chameleon.exception.ChameleonProgrammerException;

public class DeclaratorSelector extends DeclarationSelector{
	
	public DeclaratorSelector(DeclarationSelector selector) {
		if(selector == null) {
			throw new ChameleonProgrammerException("The wrapped selector of a declarator selector cannot be null.");
		}
		_selector = selector;
	}
	
	@Override
  public Declaration actualDeclaration(Declaration declarator) throws LookupException {
  	return declarator;
  }

  private DeclarationSelector _selector;

  public List selection(List selectionCandidates) throws LookupException {
  	return _selector.declarators(selectionCandidates);
  }
  
	@Override
	public boolean selectedRegardlessOfName(Declaration declaration) throws LookupException {
		return _selector.selectedRegardlessOfName(declaration);
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
	public boolean selectedBasedOnName(Signature signature) throws LookupException {
		return _selector.selectedBasedOnName(signature);
	}

	@Override
	public String selectionName() throws LookupException {
		return _selector.selectionName();
	}

}
