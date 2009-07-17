package chameleon.core.expression;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rejuse.association.Reference;

import chameleon.core.MetamodelException;
import chameleon.core.declaration.Declaration;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.reference.CrossReference;
import chameleon.core.type.Type;
import chameleon.core.variable.Variable;
import chameleon.util.Util;

/**
 * @author Marko van Dooren
 */
public class VariableReference extends Expression<VariableReference> implements Assignable<VariableReference,Element>, CrossReference<VariableReference,Element> {

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
  	if(target != null) {
      _target.connectTo(target.parentLink());
  	} else {
  		_target.connectTo(null);
  	}
  }

  public Variable getVariable() throws LookupException {
    Variable result = (Variable)getTarget().getElement();
    if(result == null) {
      throw new LookupException("Lookup of variable reference returned null: "+getTarget().getName(), this);
    }
    return result;
  }

  protected Type actualType() throws LookupException {
    return getVariable().getType();
  }

  public boolean superOf(InvocationTarget target) throws LookupException {
    return (target instanceof VariableReference) && getTarget().compatibleWith(((VariableReference)target).getTarget());
  }

  public VariableReference clone() {
    InvocationTarget target = null;
    if(getTarget() != null) {
      target = getTarget().clone();
    }
    return new VariableReference((NamedTarget)target);
  }

  public void prefix(InvocationTarget target) throws LookupException {
    getTarget().prefixRecursive(target);
  }
  
//  public void substituteParameter(String name, Expression expr) throws MetamodelException {
//    getTarget().substituteParameter(name, expr);
//  }

  public Set<Type> getDirectExceptions() throws LookupException {
    Set<Type> result = new HashSet<Type>();
    if(getTarget() != null) {
      Util.addNonNull(language().getNullInvocationException(), result);
    }
    return result;
  }
  
  public List<? extends Element> children() {
    return Util.createNonNullList(getTarget());
  }

	public Declaration getElement() throws LookupException {
		return getVariable();
	}

//  public AccessibilityDomain getAccessibilityDomain() throws MetamodelException {
//    return getTarget().getAccessibilityDomain();
//  }
  
}
