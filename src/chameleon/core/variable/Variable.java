package chameleon.core.variable;

import chameleon.core.declaration.Definition;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.declaration.TargetDeclaration;
import chameleon.core.element.Element;
import chameleon.core.expression.Expression;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.modifier.ElementWithModifiers;
import chameleon.oo.type.DeclarationWithType;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeReference;
import chameleon.oo.type.VariableOrType;

/**
 * @author Marko van Dooren
 */
public interface Variable<E extends Variable<E,P,F>, P extends Element, F extends Variable> 
                extends ElementWithModifiers<E,Element>, Definition<E, Element, SimpleNameSignature, F>, 
                VariableOrType<E,Element,SimpleNameSignature,F>, DeclarationWithType<E,Element,SimpleNameSignature,F> {

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
	
//	public List<Modifier> modifiers();
//
//	public void addModifier(Modifier modifier);
//
//	public void removeModifier(Modifier modifier);

  public E clone();

  public LookupStrategy targetContext() throws LookupException;


  public Variable selectionDeclaration();
}
