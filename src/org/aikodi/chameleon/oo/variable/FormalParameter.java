package org.aikodi.chameleon.oo.variable;

import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.scope.LexicalScope;
import org.aikodi.chameleon.core.scope.Scope;
import org.aikodi.chameleon.core.variable.VariableContainer;
import org.aikodi.chameleon.exception.ModelException;
import org.aikodi.chameleon.oo.member.DeclarationWithParametersHeader;
import org.aikodi.chameleon.oo.method.Method;
import org.aikodi.chameleon.oo.type.TypeReference;

/**
 * @author Marko van Dooren
 */
public class FormalParameter extends RegularVariable {

  public FormalParameter(String name, TypeReference type) {
    super(name, type,null);
  }

	/**
   * @param parameter
   * @return
   */
  public boolean compatibleWith(FormalParameter parameter) throws LookupException {
  	boolean result = false;
  	if((parent() instanceof DeclarationWithParametersHeader) && (parameter != null) && (parameter.parent() instanceof DeclarationWithParametersHeader)) {
    	DeclarationWithParametersHeader header = (DeclarationWithParametersHeader) parent();
    	Method method = header.nearestAncestor(Method.class);
    	//BUG: the following should be parameter.parent(). Fix after removing the parameters.
    	DeclarationWithParametersHeader otherHeader = (DeclarationWithParametersHeader) parent();
    	Method otherMethod = otherHeader.nearestAncestor(Method.class); //REFACTORING
    	result = method.overrides(otherMethod) &&
      method.formalParameters().indexOf(this) == otherMethod.formalParameters().indexOf(parameter); 
  	}
    return result; 
  }

  @Override
protected FormalParameter cloneSelf() {
    return new FormalParameter(name(), null);
  }
  
	@Override
   public Scope scope() throws ModelException {
		return new LexicalScope(nearestAncestor(VariableContainer.class).variableScopeElement());
	}

	@Override
   public FormalParameter actualDeclaration() throws LookupException {
		return this;
	}

}
