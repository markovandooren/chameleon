package chameleon.core.variable;

import java.util.List;

import org.rejuse.logic.ternary.Ternary;
import org.rejuse.property.Property;
import org.rejuse.property.PropertySet;

import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.declaration.TargetDeclaration;
import chameleon.core.element.Element;
import chameleon.core.expression.Expression;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.modifier.Modifier;
import chameleon.core.modifier.ModifierContainer;
import chameleon.core.type.Type;
import chameleon.core.type.TypeReference;
import chameleon.core.type.VariableOrType;

/**
 * @author Marko van Dooren
 */
public interface Variable<E extends Variable<E,P,F>, P extends DeclarationContainer, F extends Variable> 
                extends Element<E,P>, 
                VariableOrType<E,P,SimpleNameSignature,F>,ModifierContainer<E,P>, TargetDeclaration<E,P,SimpleNameSignature,F> {

  public void setSignature(SimpleNameSignature signature);
  
	public Expression getInitialization();

	public void setInitialization(Expression expr);
  
  /**
   * Return the signature of this member.
   */
  public SimpleNameSignature signature();
  
  /**
   * Return the name of this variable.
   */
  public String getName();

  public abstract TypeReference getTypeReference();
  
  public abstract void setTypeReference(TypeReference ref);

  public Type getType() throws LookupException;



	/*************
	 * MODIFIERS *
	 *************/
	
	public List<Modifier> modifiers();

	public void addModifier(Modifier modifier);

	public void removeModifier(Modifier modifier);

  public E clone();

  public Ternary is(Property<Element> property);
 
  public PropertySet<Element> declaredProperties();
  
  public LookupStrategy targetContext() throws LookupException;


  public Variable resolveForMatch();
}
