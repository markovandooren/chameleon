package chameleon.core.expression;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rejuse.association.Reference;

import chameleon.core.MetamodelException;
import chameleon.core.scope.Scope;
import chameleon.core.type.Type;
import chameleon.core.type.VariableOrType;
import chameleon.util.Util;

/**
 * @author Marko van Dooren
 */
public class VariableOrTypeReference extends Expression<VariableOrTypeReference> implements InvocationTargetContainer<VariableOrTypeReference,ExpressionContainer>, Assignable<VariableOrTypeReference,ExpressionContainer> {
    
  public VariableOrTypeReference(InvocationTarget target) {
      setTarget(target);
  }

  public Type getType() throws MetamodelException {
    return getVariableOrType().getType();
  }

  public VariableOrType getVariableOrType() throws MetamodelException {
    return (VariableOrType)((NamedTarget)getTarget()).getElement();
  }

	/**
	 * TARGET
	 */
	private Reference<VariableOrTypeReference,InvocationTarget> _target = new Reference<VariableOrTypeReference,InvocationTarget>(this);

  public InvocationTarget getTarget() {
    return _target.getOtherEnd();
  }

  public void setTarget(InvocationTarget target) {
  	if(_target != null) {
      _target.connectTo(target.parentLink());
  	} else {
  		_target.connectTo(null);
  	}
  }

  public boolean superOf(InvocationTarget target) throws MetamodelException {
    return (
             (target instanceof VariableOrTypeReference) && 
             getTarget().compatibleWith(((VariableOrTypeReference)target).getTarget())
           ) ||
           (
             (target instanceof VariableReference) && 
             getTarget().compatibleWith(((VariableReference)target).getTarget())
           ) ||
           (
             (target instanceof Expression) &&
             getTarget().compatibleWith(target)
           );
  }
  
  public boolean subOf(InvocationTarget target) throws MetamodelException {
    return (target instanceof VariableReference) && ((VariableReference)target).getTarget().compatibleWith(getTarget());
  }

  public VariableOrTypeReference clone() {
    InvocationTarget target = null;
    if(getTarget() != null) {
      target = getTarget().clone();
    }
    return new VariableOrTypeReference(target);
  }

  public void prefix(InvocationTarget target) throws MetamodelException {
    getTarget().prefixRecursive(target);
  }
  
//  public void substituteParameter(String name, Expression expr) throws MetamodelException {
//    getTarget().substituteParameter(name, expr);
//  }
  
  public Set getDirectExceptions() throws MetamodelException {
    Set result = new HashSet();
    if(getTarget() != null) {
      Util.addNonNull(language().getNullInvocationException(), result);
    }
    return result;
  }
  
  public List children() {
    return Util.createNonNullList(getTarget());
  }

//  public AccessibilityDomain getAccessibilityDomain() throws MetamodelException {
//    return getTarget().getAccessibilityDomain();
//  }
	
}
