package chameleon.core.expression;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rejuse.association.SingleAssociation;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.Signature;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.declaration.TargetDeclaration;
import chameleon.core.element.Element;
import chameleon.core.language.ObjectOrientedLanguage;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.lookup.SelectorWithoutOrder;
import chameleon.core.lookup.Target;
import chameleon.core.reference.CrossReferenceImpl;
import chameleon.core.statement.CheckedExceptionList;
import chameleon.core.type.Type;
import chameleon.core.variable.FormalParameter;
import chameleon.core.variable.MemberVariable;
import chameleon.core.variable.Variable;
import chameleon.util.Util;
public class NamedTarget extends CrossReferenceImpl<NamedTarget,Element,TargetDeclaration> implements InvocationTarget<NamedTarget,Element> {

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
  	_signature=new SimpleNameSignature(""); // name will be set correctly in setName().
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
  	_signature=new SimpleNameSignature(""); // name will be set correctly in setName().
  	setName(identifier);
  	setTarget(target);
  }
  
	/**
	 * TARGET
	 */
	private SingleAssociation<InvocationTarget,InvocationTarget> _target = new SingleAssociation<InvocationTarget,InvocationTarget>(this);

  public InvocationTarget<?,?> getTarget() {
    return _target.getOtherEnd();
  }

  public void setTarget(InvocationTarget target) {
    if (target != null) {
      _target.connectTo(target.parentLink());
    }
    else {
      _target.connectTo(null);
    }
  }

  public List<Element> children() {
  	return Util.createNonNullList(getTarget());
  }

  
  /***********
   * CONTEXT *
   ***********/
   
  @SuppressWarnings("unchecked")
  protected <X extends Declaration> X getElement(DeclarationSelector<X> selector) throws LookupException {
    InvocationTarget target = getTarget();
    X result;
    if(target != null) {
      result = target.targetContext().lookUp(selector);//findElement(getName());
    } else {
      result = lexicalLookupStrategy().lookUp(selector);//findElement(getName());
    }
    if(result != null) {
      return result;
    } else {
    	// repeat for debugging purposes
      if(target != null) {
        result = target.targetContext().lookUp(selector);//findElement(getName());
      } else {
        result = lexicalLookupStrategy().lookUp(selector);//findElement(getName());
      }
    	throw new LookupException("Lookup of named target with name: "+getName()+" returned null.");
    }
  }
  
  public DeclarationSelector<TargetDeclaration> selector() {
  	return new SelectorWithoutOrder<TargetDeclaration>(new SelectorWithoutOrder.SignatureSelector() {
			public Signature signature() {
				return _signature;
			}
		}, TargetDeclaration.class);
  }

  /********
   * NAME *
   ********/

  public String getName() {
    return _name;
  }

  public void setName(String name) {
    _name = name;
    _signature.setName(name);
  }

  private String _name;

	private SimpleNameSignature _signature;

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

  public void prefixRecursive(InvocationTarget target) throws LookupException {
    prefix(target);
  }

  @SuppressWarnings("unchecked")
  public Set getDirectExceptions() throws LookupException {
    Set result = new HashSet();
    if(getTarget() != null || Util.getSecondPart(getName()) != null) {
      Util.addNonNull(language(ObjectOrientedLanguage.class).getNullInvocationException(), result);
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
      return new CheckedExceptionList();
    }
  }

  public CheckedExceptionList getAbsCEL() throws LookupException {
    if(getTarget() != null) {
      return getTarget().getAbsCEL();
    }
    else {
      return new CheckedExceptionList();
    }
  }

  public LookupStrategy targetContext() throws LookupException {
    return getElement().targetContext();
  }
}
