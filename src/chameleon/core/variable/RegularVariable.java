package chameleon.core.variable;

import java.util.ArrayList;
import java.util.List;

import org.rejuse.association.OrderedReferenceSet;
import org.rejuse.association.Reference;
import org.rejuse.java.collections.Visitor;
import org.rejuse.logic.ternary.Ternary;
import org.rejuse.property.Property;
import org.rejuse.property.PropertySet;

import chameleon.core.MetamodelException;
import chameleon.core.context.TargetContext;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.expression.Expression;
import chameleon.core.expression.ExpressionContainer;
import chameleon.core.modifier.Modifier;
import chameleon.core.statement.CheckedExceptionList;
import chameleon.core.statement.ExceptionSource;
import chameleon.core.type.Type;
import chameleon.core.type.TypeDescendantImpl;
import chameleon.core.type.TypeReference;
import chameleon.util.Util;

public abstract class RegularVariable<E extends RegularVariable<E,P>, P extends VariableContainer> 
       extends VariableImpl<E,P> implements ExpressionContainer<E,P>, ExceptionSource<E,P> {

	public RegularVariable(SimpleNameSignature sig, TypeReference typeRef, Expression init) {
		super(sig);
    setTypeReference(typeRef);
    setInitialization(init);
	}

	/**
	 * TYPE
	 */
	private Reference<Variable,TypeReference> _typeReference = new Reference<Variable,TypeReference>(this);


  public TypeReference getTypeReference() {
    return _typeReference.getOtherEnd();
  }

  public void setTypeReference(TypeReference type) {
    _typeReference.connectTo(type.getParentLink());
  }

	/**
	 * INITIALIZATION EXPRESSION 
	 */
  
	private Reference<RegularVariable,Expression> _init = new Reference<RegularVariable,Expression>(this);

	public Expression getInitialization() {
    return _init.getOtherEnd();
  }
  
  public void setInitialization(Expression expr) {
    if(expr != null) {
      _init.connectTo(expr.getParentLink());
    }
    else {
      _init.connectTo(null);
    }
  }
  
  public CheckedExceptionList getCEL() throws MetamodelException {
    if(getInitialization() != null) {
      return getInitialization().getCEL();
    }
    else {
      return new CheckedExceptionList(getNamespace().language());
    }
  }

  public CheckedExceptionList getAbsCEL() throws MetamodelException {
    if(getInitialization() != null) {
      return getInitialization().getAbsCEL();
    }
    else {
      return new CheckedExceptionList(getNamespace().language());
    }
  }

 /*@
   @ also public behavior
   @
   @ post \result.containsAll(modifiers());
   @ post getTypeReference() != null ==> \result.contains(getTypeReference());
   @ post getInitialization() != null ==> \result.contains(getInitialization());
   @ post signature() != null ==> \result.contains(signature());
   @*/
  public List<Element> getChildren() {
    List result = new ArrayList<Element>();
    Util.addNonNull(getInitialization(), result);
    result.addAll(modifiers());
    Util.addNonNull(signature(), result);
    Util.addNonNull(getInitialization(), result);
    return result;
  }
	/*************
	 * MODIFIERS *
	 *************/
	
	private OrderedReferenceSet<Variable, Modifier> _modifiers = new OrderedReferenceSet<Variable, Modifier>(this);

	public List<Modifier> modifiers() {
		return _modifiers.getOtherEnds();
	}

	public void addModifier(Modifier modifier) {
		if ((!_modifiers.contains(modifier.getParentLink())) && (modifier != null)) {
			_modifiers.add(modifier.getParentLink());
		}
	}

	public void removeModifier(Modifier modifier) {
		_modifiers.remove(modifier.getParentLink());
	}

	public boolean hasModifier(Modifier modifier) {
		return _modifiers.getOtherEnds().contains(modifier);
	}


  /**
   * Return the name of this variable.
   */
  public String getName() {
    return signature().getName();
  }

  public Type getType() throws MetamodelException {
    Type result = getTypeReference().getType();
    if(result != null) {
      return result;
    }
    else {
      getTypeReference().getType();
      throw new MetamodelException();
    }
  }

  /*@
  @ also public behavior
  @
  @ post \result == getParent().getNearestType();
  @*/
 public Type getNearestType() {
   return getParent().getNearestType();
 }

 public E clone() {
   final E result = cloneThis();
   new Visitor<Modifier>() {
     public void visit(Modifier element) {
       result.addModifier(element.clone());
     }
   }.applyTo(modifiers());
   return result;
 }

 protected abstract E cloneThis();

 public Ternary is(Property<Element> property) {
   PropertySet<Element> declared = declaredProperties();
   if((property).appliesTo(this)) {
     declared.add(property);
   }
   return declared.implies(property);
 }

 public PropertySet<Element> declaredProperties() {
   PropertySet<Element> result = new PropertySet<Element>();
   for(Modifier modifier:modifiers()) {
     result.addAll(modifier.impliedProperties());
   }
   return result;
 }
 
 public TargetContext targetContext() throws MetamodelException {
   return getType().targetContext();
 }


 public Variable resolve() {
 	return this;
 }

}
