package chameleon.core.declaration;

import chameleon.core.element.Element;
import chameleon.core.element.ElementImpl;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.property.ChameleonProperty;
import chameleon.core.scope.Scope;
import chameleon.core.scope.ScopeProperty;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.exception.ModelException;

public abstract class DeclarationImpl extends ElementImpl implements Declaration {

	@Override
	public String name() {
		return signature().name();
	}

	@Override
	public void setName(String name) {
		signature().setName(name);
	}

	@Override
	public Declaration selectionDeclaration() throws LookupException {
		return this;
	}

	@Override
	public Declaration actualDeclaration() throws LookupException {
		return this;
	}

	@Override
	public Declaration declarator() {
		return this;
	}

  public Scope scope() throws ModelException {
  	Scope result = null;
  	ChameleonProperty scopeProperty = property(language().SCOPE_MUTEX());
  	if(scopeProperty instanceof ScopeProperty) {
  		result = ((ScopeProperty)scopeProperty).scope(this);
  	} else if(scopeProperty != null){
  		throw new ChameleonProgrammerException("Scope property is not a ScopeProperty");
  	}
  	return result;
  }

	@Override
	public boolean complete() throws LookupException {
		return true;
	}

}
