package chameleon.core.variable;



import chameleon.core.MetamodelException;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.method.Method;
import chameleon.core.type.TypeReference;

/**
 * @author Marko van Dooren
 */
public class FormalParameter extends RegularVariable<FormalParameter,VariableContainer> {

  public FormalParameter(SimpleNameSignature sig, TypeReference type) {
    super(sig, type);
  }

  /**
   * @param parameter
   * @return
   */
  public boolean compatibleWith(FormalParameter parameter) throws MetamodelException {
    return (parameter != null) &&
           (getParent() instanceof Method) &&
           (parameter.getParent() instanceof Method) &&
           ((Method)getParent()).overrides((Method)parameter.getParent()) &&
           ((Method)getParent()).getParameters().indexOf(this) == ((Method)parameter.getParent()).getParameters().indexOf(parameter);
  }

  public FormalParameter cloneThis() {
    return new FormalParameter(signature().clone(), (TypeReference)getTypeReference().clone());
  }

//  public AccessibilityDomain getAccessibilityDomain() throws MetamodelException {
//    return getType().getTypeAccessibilityDomain();
//  }

}
