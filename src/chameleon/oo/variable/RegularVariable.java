package chameleon.oo.variable;

import java.util.List;

import org.rejuse.association.Association;
import org.rejuse.java.collections.Visitor;
import org.rejuse.property.PropertySet;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.lookup.LocalLookupStrategy;
import chameleon.core.lookup.LookupException;
import chameleon.core.modifier.Modifier;
import chameleon.core.property.ChameleonProperty;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.oo.expression.Expression;
import chameleon.oo.statement.CheckedExceptionList;
import chameleon.oo.statement.ExceptionSource;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeReference;
import chameleon.util.association.Multi;
import chameleon.util.association.Single;

public abstract class RegularVariable extends VariableImpl implements ExceptionSource {

	public RegularVariable(SimpleNameSignature sig, TypeReference typeRef, Expression init) {
		super(sig);
    setTypeReference(typeRef);
    setInitialization(init);
	}

	/**
	 * TYPE
	 */
	private Single<TypeReference> _typeReference = new Single<TypeReference>(this);


  public TypeReference getTypeReference() {
    return _typeReference.getOtherEnd();
  }

  public void setTypeReference(TypeReference type) {
    set(_typeReference,type);
  }

	/**
	 * INITIALIZATION EXPRESSION 
	 */
  
	private Single<Expression> _init = new Single<Expression>(this,false);

	public Expression getInitialization() {
    return _init.getOtherEnd();
  }
  
  public void setInitialization(Expression expr) {
    set(_init,expr);
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

	/*************
	 * MODIFIERS *
	 *************/
	
	private Multi<Modifier> _modifiers = new Multi<Modifier>(this);

	public List<Modifier> modifiers() {
		return _modifiers.getOtherEnds();
	}

	public void addModifier(Modifier modifier) {
		if ((modifier != null) && (!_modifiers.contains((Association)modifier.parentLink()))) {
			add(_modifiers,modifier);
		}
	}
	
	public void addAllModifiers(List<Modifier> modifiers) {
		for(Modifier modifier: modifiers) {
			addModifier(modifier);
		}
	}

	public void removeModifier(Modifier modifier) {
		remove(_modifiers,modifier);
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
    Type result = getTypeReference().getElement();
    if(result != null) {
      return result;
    }
    else {
      getTypeReference().getElement();
      throw new LookupException("getType on regular variable returned null.");
    }
  }

 public RegularVariable clone() {
   final RegularVariable result = cloneThis();
   new Visitor<Modifier>() {
     public void visit(Modifier element) {
       result.addModifier(element.clone());
     }
   }.applyTo(modifiers());
   return result;
 }

 protected abstract RegularVariable cloneThis();

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
 
 public LocalLookupStrategy targetContext() throws LookupException {
   return getType().targetContext();
 }


 public Variable selectionDeclaration() {
 	return this;
 }

 public Declaration declarator() {
	 return this;
 }


}
