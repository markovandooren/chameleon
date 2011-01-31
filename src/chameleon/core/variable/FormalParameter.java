package chameleon.core.variable;

import chameleon.core.declaration.DeclarationWithParametersHeader;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.lookup.LookupException;
import chameleon.core.method.Method;
import chameleon.core.scope.LexicalScope;
import chameleon.core.scope.Scope;
import chameleon.exception.ModelException;
import chameleon.oo.type.TypeReference;

/**
 * @author Marko van Dooren
 */
public class FormalParameter extends RegularVariable<FormalParameter,VariableContainer,FormalParameter> {

  public FormalParameter(SimpleNameSignature sig, TypeReference type) {
    super(sig, type,null);
  }

  public FormalParameter(String string, TypeReference tref) {
		this(new SimpleNameSignature(string), tref);
	}

	/**
   * @param parameter
   * @return
   */
  public boolean compatibleWith(FormalParameter parameter) throws LookupException {
  	boolean result = false;
  	if((parent() instanceof DeclarationWithParametersHeader) && (parameter != null) && (parameter.parent() instanceof DeclarationWithParametersHeader)) {
    	DeclarationWithParametersHeader<?,? extends Method,?> header = (DeclarationWithParametersHeader<?, ? extends Method, ?>) parent();
    	Method method = header.parent();
    	DeclarationWithParametersHeader<?,?,?> otherHeader = (DeclarationWithParametersHeader<?, ?, ?>) parent();
    	Method otherMethod = header.parent();
    	result = method.overrides(otherMethod) &&
      method.formalParameters().indexOf(this) == otherMethod.formalParameters().indexOf(parameter); 
  	}
    return result; 
  }

  public FormalParameter cloneThis() {
    return new FormalParameter(signature().clone(), (TypeReference)getTypeReference().clone());
  }
  
	public Scope scope() throws ModelException {
		return new LexicalScope(nearestAncestor(VariableContainer.class).variableScopeElement());
	}

	public FormalParameter actualDeclaration() throws LookupException {
		return this;
	}

}
