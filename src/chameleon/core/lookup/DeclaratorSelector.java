package chameleon.core.lookup;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.Signature;
import chameleon.core.relation.WeakPartialOrder;

public class DeclaratorSelector extends DeclarationSelector{
	
	public DeclaratorSelector(DeclarationSelector selector) {
		_selector = selector;
	}
	
	@Override
  public Declaration actualDeclaration(Declaration declarator) throws LookupException {
  	return declarator;
  }

  private DeclarationSelector _selector;

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

}
