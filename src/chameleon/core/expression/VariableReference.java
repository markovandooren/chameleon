package chameleon.core.expression;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rejuse.association.Reference;

import chameleon.core.MetamodelException;
import chameleon.core.element.Element;
import chameleon.core.type.Type;
import chameleon.core.variable.Variable;
import chameleon.util.Util;

/**
 * @author Marko van Dooren
 */
public class VariableReference extends Expression<VariableReference> implements Assignable<VariableReference,ExpressionContainer>, InvocationTargetContainer<VariableReference,ExpressionContainer> {

  public VariableReference(NamedTarget target) {
	  setTarget(target);
  }
  
	/**
	 * TARGET
	 */
	private Reference<VariableReference,NamedTarget> _target = new Reference<VariableReference,NamedTarget>(this);

  public NamedTarget getTarget() {
    return _target.getOtherEnd();
  }

  public void setTarget(NamedTarget target) {
    _target.connectTo(target.getParentLink());
  }

  public Variable getVariable() throws MetamodelException {
    Variable result = (Variable)getTarget().getElement();
    if(result == null) {
      throw new MetamodelException();
    }
    return result;
  }

  public Type getType() throws MetamodelException {
    return getVariable().getType();
  }

  public boolean superOf(InvocationTarget target) throws MetamodelException {
    return (target instanceof VariableReference) && getTarget().compatibleWith(((VariableReference)target).getTarget());
  }

  public VariableReference clone() {
    InvocationTarget target = null;
    if(getTarget() != null) {
      target = getTarget().clone();
    }
    return new VariableReference((NamedTarget)target);
  }

  public void prefix(InvocationTarget target) throws MetamodelException {
    getTarget().prefixRecursive(target);
  }
  
  public void substituteParameter(String name, Expression expr) throws MetamodelException {
    getTarget().substituteParameter(name, expr);
  }

  public Set<Type> getDirectExceptions() throws MetamodelException {
    Set<Type> result = new HashSet<Type>();
    if(getTarget() != null) {
      Util.addNonNull(language().getNullInvocationException(), result);
    }
    return result;
  }
  
  public List<? extends Element> getChildren() {
    return Util.createNonNullList(getTarget());
  }

//  public AccessibilityDomain getAccessibilityDomain() throws MetamodelException {
//    return getTarget().getAccessibilityDomain();
//  }
  
}
