package chameleon.core.variable;

import java.util.ArrayList;
import java.util.List;

import org.rejuse.association.OrderedMultiAssociation;
import org.rejuse.association.SingleAssociation;
import org.rejuse.java.collections.Visitor;
import org.rejuse.property.PropertySet;

import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.ChameleonProgrammerException;
import chameleon.core.element.Element;
import chameleon.core.expression.Expression;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.modifier.Modifier;
import chameleon.core.property.ChameleonProperty;
import chameleon.core.statement.CheckedExceptionList;
import chameleon.core.statement.ExceptionSource;
import chameleon.core.type.Type;
import chameleon.core.type.TypeReference;
import chameleon.util.Util;

public abstract class RegularVariable<E extends RegularVariable<E,P,F>, P extends DeclarationContainer, F extends Variable> 
       extends VariableImpl<E,P,F> implements ExceptionSource<E,P> {

	public RegularVariable(SimpleNameSignature sig, TypeReference typeRef, Expression init) {
		super(sig);
    setTypeReference(typeRef);
    setInitialization(init);
	}

	/**
	 * TYPE
	 */
	private SingleAssociation<Variable,TypeReference> _typeReference = new SingleAssociation<Variable,TypeReference>(this);


  public TypeReference getTypeReference() {
    return _typeReference.getOtherEnd();
  }

  public void setTypeReference(TypeReference type) {
    _typeReference.connectTo(type.parentLink());
  }

	/**
	 * INITIALIZATION EXPRESSION 
	 */
  
	private SingleAssociation<RegularVariable,Expression> _init = new SingleAssociation<RegularVariable,Expression>(this);

	public Expression getInitialization() {
    return _init.getOtherEnd();
  }
  
  public void setInitialization(Expression expr) {
    if(expr != null) {
      _init.connectTo(expr.parentLink());
    }
    else {
      _init.connectTo(null);
    }
  }
  
  public CheckedExceptionList getCEL() throws LookupException {
    if(getInitialization() != null) {
      return getInitialization().getCEL();
    }
    else {
      return new CheckedExceptionList();
    }
  }

  public CheckedExceptionList getAbsCEL() throws LookupException {
    if(getInitialization() != null) {
      return getInitialization().getAbsCEL();
    }
    else {
      return new CheckedExceptionList();
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
  public List<Element> children() {
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
	
	private OrderedMultiAssociation<Variable, Modifier> _modifiers = new OrderedMultiAssociation<Variable, Modifier>(this);

	public List<Modifier> modifiers() {
		return _modifiers.getOtherEnds();
	}

	public void addModifier(Modifier modifier) {
		if ((modifier != null) && (!_modifiers.contains(modifier.parentLink()))) {
			_modifiers.add(modifier.parentLink());
		}
	}
	
	public void addAllModifiers(List<Modifier> modifiers) {
		for(Modifier modifier: modifiers) {
			addModifier(modifier);
		}
	}

	public void removeModifier(Modifier modifier) {
		_modifiers.remove(modifier.parentLink());
	}

	public boolean hasModifier(Modifier modifier) {
		return _modifiers.getOtherEnds().contains(modifier);
	}

	// copied from TypeElementImpl
  public void addModifiers(List<Modifier> modifiers) {
  	if(modifiers == null) {
  		throw new ChameleonProgrammerException("List passed to addModifiers is null");
  	} else {
  		for(Modifier modifier: modifiers) {
  			addModifier(modifier);
  		}
  	}
  }


  /**
   * Return the name of this variable.
   */
  public String getName() {
    return signature().name();
  }

  public Type getType() throws LookupException {
    Type result = getTypeReference().getType();
    if(result != null) {
      return result;
    }
    else {
      getTypeReference().getType();
      throw new LookupException("getType on regular variable returned null.");
    }
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

// public Ternary is(Property<Element> property) {
//   PropertySet<Element> declared = declaredProperties();
//   if((property).appliesTo(this)) {
//     declared.add(property);
//   }
//   return declared.implies(property);
// }

 public PropertySet<Element,ChameleonProperty> declaredProperties() {
   PropertySet<Element,ChameleonProperty> result = new PropertySet<Element,ChameleonProperty>();
   for(Modifier modifier:modifiers()) {
     result.addAll(modifier.impliedProperties());
   }
   return result;
 }
 
 public LookupStrategy targetContext() throws LookupException {
   return getType().targetContext();
 }


 public Variable selectionDeclaration() {
 	return this;
 }

}
