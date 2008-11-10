package chameleon.core.variable;

import java.util.List;

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
import chameleon.core.type.TypeDescendant;
import chameleon.core.type.TypeReference;
import chameleon.core.type.VariableOrType;
import chameleon.util.Util;

/**
 * @author Marko van Dooren
 */
public interface Variable<E extends Variable<E,P>, P extends VariableContainer> 
                extends TypeDescendant<E,P>, 
                VariableOrType<E,P>,ModifierContainer<E,P>, TargetDeclaration<E,P,SimpleNameSignature> {

  public void setSignature(SimpleNameSignature signature);
  
  /**
   * Return the signature of this member.
   */
  public SimpleNameSignature signature();
  
  /**
   * Return the name of this variable.
   */
  public String getName();

  public abstract TypeReference getTypeReference();

  public Type getType() throws MetamodelException;



	/*************
	 * MODIFIERS *
	 *************/
	
	public List<Modifier> modifiers();

	public void addModifier(Modifier modifier);

	public void removeModifier(Modifier modifier);

 /*@
   @ also public behavior
   @
   @ post \result == getParent().getNearestType();
   @*/
  public Type getNearestType();

  public E clone();

  public Ternary is(Property<Element> property);
 
  public PropertySet<Element> declaredProperties();
  
  public TargetContext targetContext() throws MetamodelException;


  public Variable resolve();
}
