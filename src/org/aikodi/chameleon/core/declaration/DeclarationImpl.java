package org.aikodi.chameleon.core.declaration;

import java.util.Collection;

import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.SelectionResult;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.chameleon.core.scope.Scope;
import org.aikodi.chameleon.core.scope.ScopeProperty;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.exception.ModelException;

import be.kuleuven.cs.distrinet.rejuse.predicate.AbstractPredicate;

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

  @Override
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

  @Override
  public boolean sameSignatureAs(Declaration declaration) throws LookupException {
  	return signature().sameAs(declaration.signature());
  }
  

}
