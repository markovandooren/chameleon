package org.aikodi.chameleon.oo.variable;

import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LocalLookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.modifier.Modifier;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.core.variable.Variable;
import org.aikodi.chameleon.core.variable.VariableImpl;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.oo.expression.Expression;
import org.aikodi.chameleon.oo.statement.CheckedExceptionList;
import org.aikodi.chameleon.oo.statement.ExceptionSource;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeReference;
import org.aikodi.chameleon.util.association.Multi;
import org.aikodi.chameleon.util.association.Single;

import be.kuleuven.cs.distrinet.rejuse.association.Association;
import be.kuleuven.cs.distrinet.rejuse.property.PropertySet;

public abstract class RegularVariable extends VariableImpl implements ExceptionSource {

	public RegularVariable(String name, TypeReference typeRef, Expression init) {
		super(name);
    setTypeReference(typeRef);
    setInitialization(init);
	}

	/**
	 * TYPE
	 */
	private Single<TypeReference> _typeReference = new Single<TypeReference>(this);


  @Override
public TypeReference getTypeReference() {
    return _typeReference.getOtherEnd();
  }

  @Override
public void setTypeReference(TypeReference type) {
    set(_typeReference,type);
  }

	/**
	 * INITIALIZATION EXPRESSION 
	 */
  
	private Single<Expression> _init = new Single<Expression>(this,false);

	@Override
   public Expression getInitialization() {
    return _init.getOtherEnd();
  }
  
  @Override
public void setInitialization(Expression expr) {
    set(_init,expr);
  }
  
	/*************
	 * MODIFIERS *
	 *************/
	
	private Multi<Modifier> _modifiers = new Multi<Modifier>(this);

	@Override
   public List<Modifier> modifiers() {
		return _modifiers.getOtherEnds();
	}

	@Override
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

	@Override
   public void removeModifier(Modifier modifier) {
		remove(_modifiers,modifier);
	}

	public boolean hasModifier(Modifier modifier) {
		return _modifiers.getOtherEnds().contains(modifier);
	}

	// copied from TypeElementImpl
  @Override
public void addModifiers(List<Modifier> modifiers) {
  	if(modifiers == null) {
  		throw new ChameleonProgrammerException("List passed to addModifiers is null");
  	} else {
  		for(Modifier modifier: modifiers) {
  			addModifier(modifier);
  		}
  	}
  }


  @Override
public Type getType() throws LookupException {
  	Type result = getTypeReference().getElement();
  	if(result != null) {
  		return result;
  	}
  	else {
  		throw new LookupException("getType on regular variable returned null.");
  	}
  }

 // FIXME Code duplication from ElementWithModifiersImpl
 @Override
public PropertySet<Element,ChameleonProperty> declaredProperties() {
   PropertySet<Element,ChameleonProperty> result = new PropertySet<Element,ChameleonProperty>();
   for(Modifier modifier:modifiers()) {
     result.addAll(modifier.impliedProperties());
   }
   return result;
 }
 
 @Override
public LocalLookupContext<?> targetContext() throws LookupException {
   return getType().targetContext();
 }


 @Override
public Variable selectionDeclaration() {
 	return this;
 }

 @Override
public Declaration declarator() {
	 return this;
 }


}
