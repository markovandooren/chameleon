package org.aikodi.chameleon.core.declaration;

import org.aikodi.chameleon.core.modifier.ElementWithModifiersImpl;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.core.scope.Scope;
import org.aikodi.chameleon.core.scope.ScopeProperty;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.exception.ModelException;

public abstract class DeclarationImpl extends ElementWithModifiersImpl implements Declaration {

	/**
	 * {@inheritDoc}
	 */
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
 

}
