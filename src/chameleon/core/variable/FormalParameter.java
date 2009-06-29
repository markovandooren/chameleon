package chameleon.core.variable;

import chameleon.core.MetamodelException;
import chameleon.core.context.LookupException;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.method.Method;
import chameleon.core.method.MethodHeader;
import chameleon.core.scope.LexicalScope;
import chameleon.core.scope.Scope;
import chameleon.core.type.TypeReference;

/**
 * @author Marko van Dooren
 */
public class FormalParameter extends RegularVariable<FormalParameter,VariableContainer> {

  public FormalParameter(SimpleNameSignature sig, TypeReference type) {
    super(sig, type,null);
  }

  /**
   * @param parameter
   * @return
   */
  public boolean compatibleWith(FormalParameter parameter) throws LookupException {
  	boolean result = false;
  	if((parent() instanceof MethodHeader) && (parameter != null) && (parameter.parent() instanceof MethodHeader)) {
    	MethodHeader<?,?,?> header = (MethodHeader<?, ?, ?>) parent();
    	Method method = header.parent();
    	MethodHeader<?,?,?> otherHeader = (MethodHeader<?, ?, ?>) parent();
    	Method otherMethod = header.parent();
    	result = method.overrides(otherMethod) &&
      method.getParameters().indexOf(this) == otherMethod.getParameters().indexOf(parameter); 
  	}
    return result; 
  }

  public FormalParameter cloneThis() {
    return new FormalParameter(signature().clone(), (TypeReference)getTypeReference().clone());
  }
  
	public Scope scope() throws MetamodelException {
		return new LexicalScope(parent().variableScopeElement());
	}

}
