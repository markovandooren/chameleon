package chameleon.core.variable;

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
import chameleon.core.declaration.TargetDeclaration;
import chameleon.core.element.Element;
import chameleon.core.modifier.Modifier;
import chameleon.core.modifier.ModifierContainer;
import chameleon.core.type.Type;
import chameleon.core.type.TypeDescendantImpl;
import chameleon.core.type.TypeReference;
import chameleon.core.type.VariableOrType;
import chameleon.util.Util;

/**
 * @author Marko van Dooren
 */
public abstract class Variable<E extends Variable<E,P>, P extends VariableContainer> 
                extends TypeDescendantImpl<E,P> 
                implements VariableOrType<E,P>,ModifierContainer<E,P>, TargetDeclaration<E,P,SimpleNameSignature>{

  public Variable(SimpleNameSignature sig) {
    setSignature(sig);
  }
  
  public void setSignature(SimpleNameSignature signature) {
    if(signature != null) {
      _signature.connectTo(signature.getParentLink());
    } else {
      _signature.connectTo(null);
    }
  }
  
  /**
   * Return the signature of this member.
   */
  public SimpleNameSignature signature() {
    return _signature.getOtherEnd();
  }
  
  private Reference<Variable, SimpleNameSignature> _signature = new Reference<Variable, SimpleNameSignature>(this);

  /**
   * Return the name of this variable.
   */
  public String getName() {
    return signature().getName();
  }

  public abstract TypeReference getTypeReference();

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

	public boolean is(Modifier modifier) {
		return _modifiers.getOtherEnds().contains(modifier);
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

 /*@
   @ also public behavior
   @
   @ post \result.containsAll(getModifiers());
   @ post getTypeReference() != null ==> \result.contains(getTypeReference());
   @*/
  public List getChildren() {
    List result = modifiers();
    Util.addNonNull(getTypeReference(), result);
    return result;
  }
  
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


  public Variable alias(SimpleNameSignature sig) {
  	return new VariableAlias(sig,this);
  }
}
