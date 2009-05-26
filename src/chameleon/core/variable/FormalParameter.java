package chameleon.core.variable;

import chameleon.core.MetamodelException;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.method.Method;
import chameleon.core.method.MethodHeader;
import chameleon.core.scope.LexicalScope;
import chameleon.core.scope.Scope;
import chameleon.core.type.TypeReference;

/**
 * @author Marko van Dooren
 */
public class FormalParameter extends RegularVariable<FormalParameter,MethodHeader> {

  public FormalParameter(SimpleNameSignature sig, TypeReference type) {
    super(sig, type,null);
  }

  /**
   * @param parameter
   * @return
   */
  public boolean compatibleWith(FormalParameter parameter) throws MetamodelException {
    return (parameter != null) &&
           method().overrides(parameter.method()) &&
           method().getParameters().indexOf(this) == parameter.method().getParameters().indexOf(parameter);
  }

  public FormalParameter cloneThis() {
    return new FormalParameter(signature().clone(), (TypeReference)getTypeReference().clone());
  }
  
  public Method method() {
  	MethodHeader<?,?,?> par = parent();
  	return par.parent();
  }

	public Scope scope() throws MetamodelException {
		return new LexicalScope(method());
	}

//  public AccessibilityDomain getAccessibilityDomain() throws MetamodelException {
//    return getType().getTypeAccessibilityDomain();
//  }

}
