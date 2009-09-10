package chameleon.core.lookup;

import chameleon.core.declaration.Declaration;
import chameleon.core.relation.WeakPartialOrder;

public class DeclaratorSelector extends DeclarationSelector<Declaration> {
	
	public DeclaratorSelector(DeclarationSelector selector) {
		_selector = selector;
	}
	
	@Override
  public Declaration actualDeclaration(Declaration declarator) throws LookupException {
  	return declarator;
  }

  private DeclarationSelector _selector;

	@Override
	public Declaration filter(Declaration declaration) throws LookupException {
		return _selector.filter(declaration);
	}

	@Override
	public WeakPartialOrder order() {
		return _selector.order();
	}

	@Override
	public Class selectedClass() {
		return _selector.selectedClass();
	}

}
