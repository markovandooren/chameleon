package be.kuleuven.cs.distrinet.chameleon.core.declaration;

import be.kuleuven.cs.distrinet.chameleon.core.element.ElementImpl;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.SelectionResult;
import be.kuleuven.cs.distrinet.chameleon.core.property.ChameleonProperty;
import be.kuleuven.cs.distrinet.chameleon.core.scope.Scope;
import be.kuleuven.cs.distrinet.chameleon.core.scope.ScopeProperty;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.chameleon.exception.ModelException;

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
	
	/**
	 * Oracle, please fix the broken Java 7 compiler.
	 */
//	public abstract DeclarationImpl clone();

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

	@Override
	public Declaration finalDeclaration() {
		return this;
	}
	
	@Override
	public Declaration template() {
		return finalDeclaration();
	}


	@Override
	public SelectionResult updatedTo(Declaration declaration) {
		return declaration;
	}

}
