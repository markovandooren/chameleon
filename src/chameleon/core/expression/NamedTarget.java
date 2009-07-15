package chameleon.core.expression;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import chameleon.core.MetamodelException;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.declaration.TargetDeclaration;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.lookup.SelectorWithoutOrder;
import chameleon.core.lookup.Target;
import chameleon.core.reference.CrossReference;
import chameleon.core.statement.CheckedExceptionList;
import chameleon.core.type.Type;
import chameleon.core.variable.FormalParameter;
import chameleon.core.variable.MemberVariable;
import chameleon.core.variable.Variable;
import chameleon.util.Util;
public class NamedTarget extends InvocationTargetWithTarget<NamedTarget> implements CrossReference<NamedTarget,Element> {

	/**
	 * Initialize a new named target with the given fully qualified name. The
	 * constructor will split up the name at the dots if there are any.
	 * @param fqn
	 */
 /*@
   @ public behavior
   @
   @ pre fqn != null;
   @
   @*/
  public NamedTarget(String fqn) {
	  setName(Util.getLastPart(fqn));
    fqn = Util.getAllButLastPart(fqn);
    if(fqn != null) {
      setTarget(new NamedTarget(fqn)); 
    }
    //setTargetContext(new NamedTargetContext());
  }
  
  /**
   * Initialize a new named target with the given identifier as name,
   * and the given target as its target. The name should be an identifier
   * and thus not contain dots.
   * 
   * @param identifier
   * @param target
   */
  public NamedTarget(String identifier, InvocationTarget target) {
  	setName(identifier);
  	setTarget(target);
  }
  
  /***********
   * CONTEXT *
   ***********/
   
  @SuppressWarnings("unchecked")
  public TargetDeclaration getElement() throws LookupException {
    InvocationTarget target = getTarget();
    TargetDeclaration result;
    if(target != null) {
      result = target.targetContext().lookUp(selector());//findElement(getName());
    } else {
      result = lexicalContext().lookUp(selector());//findElement(getName());
    }
    if(result != null) {
      return result;
    } else {
    	// repeat for debugging purposes
      if(target != null) {
        result = target.targetContext().lookUp(selector());//findElement(getName());
      } else {
        result = lexicalContext().lookUp(selector());//findElement(getName());
      }
    	throw new LookupException("Lookup of named target with name: "+getName()+" returned null.");
    }
  }
  
  public DeclarationSelector<TargetDeclaration> selector() {
  	return new SelectorWithoutOrder(new SimpleNameSignature(getName()), TargetDeclaration.class);
  }

  /********
   * NAME *
   ********/

  public String getName() {
    return _name;
  }

  public void setName(String name) {
    _name = name;
  }

  private String _name;


  public boolean superOf(InvocationTarget target) throws LookupException {
    Target self = getElement();
    if(target instanceof Expression) {
      // If this is a type, an expression assignable to this type will be compatible
      return (self instanceof Type) && ((Expression)target).getType().assignableTo((Type)self);
    }
    if(!(target instanceof NamedTarget)) {
      return false;
    }
    NamedTarget nt = (NamedTarget)target;
    if( (getTarget() != null) && (nt.getTarget() != null)) {
      if (! getTarget().compatibleWith(nt.getTarget())) {
        return false;
      }
    } else if( ! ((getTarget() == null) && (nt.getTarget() == null))) {
      return false; 
    }
    if(self instanceof Variable) {
	    Variable vt1 = (Variable)self;
	    Target vt2 = nt.getElement();
	    if((vt2 instanceof Variable)) {
	      return compatVar(vt1, (Variable)vt2) ; // && supportVar(vt1, Util.getSecondPart(getName()), (Variable)vt2, Util.getSecondPart(nt.getName()))
	    }
	    else {
	      return false;
	    }
    }
    else {
      // self is a type
      Target other = nt.getElement();
      if(! (other instanceof Type)) {
        return false;
      }
      return ((Type)other).assignableTo((Type)self);
    }
  }
  
//  private boolean supportType(Type ct1, String n1, Type ct2, String n2) throws NotResolvedException {
//    if ((ct1 == null) && (ct2 == null)) {
//      return true;
//    } 
//    if ((ct1 == null) || (ct2 == null)) {
//      return false;
//    }
//    String first = Util.getFirstPart(n1);
//    String second = Util.getFirstPart(n2);
//    VariableOrType vt1 = ct1.findVariableOrType(first);
//    VariableOrType vt2 = ct2.findVariableOrType(second);
//    if((vt1 instanceof Type) && (vt2 instanceof Type)) {
//      return compatType((Type)vt1, (Type)vt2) && supportType((Type)vt1, Util.getSecondPart(n1), (Type)vt2, Util.getSecondPart(n2));
//    }
//    else if((vt1 instanceof Variable) && (vt2 instanceof Variable)) {
//      return compatVar((Variable)vt1, (Variable)vt2) && supportVar((Variable)vt1, Util.getSecondPart(n1), (Variable)vt2, Util.getSecondPart(n2));
//    }
//    else {
//      return false;
//    }
//  }

//  private boolean supportVar(Variable ct1, String n1, Variable ct2, String n2) throws NotResolvedException {
//    if ((ct1 == null) && (ct2 == null)) {
//      return true;
//    } 
//    if ((ct1 == null) || (ct2 == null)) {
//      return false;
//    }
//    String first = Util.getFirstPart(n1);
//    String second = Util.getFirstPart(n2);
//    Variable vt1 = ct1.getType().findVariable(first);
//    Variable vt2 = ct2.getType().findVariable(second);
//    return compatVar(vt1, vt2) && supportVar(vt1, Util.getSecondPart(n1), vt2, Util.getSecondPart(n2));
//  }

  private boolean compatVar(Variable variable, Variable variable2) throws LookupException {
    return ((variable == null) && (variable2 == null)) || 
           (variable != null) && (
               variable.equals(variable2) ||
               (
                   (variable instanceof FormalParameter) && (variable2 instanceof FormalParameter) &&
                   ((FormalParameter)variable).compatibleWith((FormalParameter)variable2)
               )
           );
  }
  
  

  public boolean compatibleWith(InvocationTarget target) throws LookupException {
    return superOf(target) || target.subOf(this);
  }

  public boolean subOf(InvocationTarget target) throws LookupException {
    return false;
  }

  public NamedTarget clone() {
    NamedTarget result = new NamedTarget(getName());
    if(getTarget()!= null) {
      result.setTarget(getTarget().clone());
    }
    return result;
  }
  
  public void prefix(InvocationTarget target) throws LookupException {
    if (getTarget() == null) {
      Target vt = getElement();
      if (vt instanceof MemberVariable) {
          setTarget(target);
      }
    }
    else {
      getTarget().prefixRecursive(target);
    }
  }
//  @SuppressWarnings("unchecked")
//  public void substituteParameter(String name, Expression expr) throws MetamodelException {
//    if(getTarget() == null) {
//      Target vt = getElement();
//      if ((vt instanceof FormalParameter) && (((FormalParameter)vt).getName().equals(name))) {
//        // replace self by the given expression
//        ((InvocationTarget)parent()).setTarget(expr);
//      }
//    }
//  }

  public void prefixRecursive(InvocationTarget target) throws LookupException {
    prefix(target);
  }

  @SuppressWarnings("unchecked")
  public Set getDirectExceptions() throws LookupException {
    Set result = new HashSet();
    if(getTarget() != null || Util.getSecondPart(getName()) != null) {
      Util.addNonNull(language().getNullInvocationException(), result);
    }
    return result;
  }

  public Set getExceptions() throws LookupException {
    Set result = getDirectExceptions();
    if(getTarget() != null) {
      result.addAll(getTarget().getExceptions());
    }
    return result;
  }

  public CheckedExceptionList getCEL() throws LookupException {
    if(getTarget() != null) {
      return getTarget().getCEL();
    }
    else {
      return new CheckedExceptionList(language());
    }
  }

  public CheckedExceptionList getAbsCEL() throws LookupException {
    if(getTarget() != null) {
      return getTarget().getAbsCEL();
    }
    else {
      return new CheckedExceptionList(language());
    }
  }

//  public AccessibilityDomain getAccessibilityDomain() throws MetamodelException {
//    Target vt = getElement();
//    AccessibilityDomain result = vt.getAccessibilityDomain();
//    if(getTarget() != null) {
//      result = result.intersect(getTarget().getAccessibilityDomain());
//    }
//    return result;
//  }

  public List<Element> children() {
  	return Util.createNonNullList(getTarget());
  }

  public LookupStrategy targetContext() throws LookupException {
    return getElement().targetContext();
  }
}
