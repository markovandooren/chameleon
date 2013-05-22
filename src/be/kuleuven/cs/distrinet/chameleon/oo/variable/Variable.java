package be.kuleuven.cs.distrinet.chameleon.oo.variable;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.SimpleNameSignature;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LocalLookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.modifier.ElementWithModifiers;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;
import be.kuleuven.cs.distrinet.chameleon.oo.type.DeclarationWithType;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;

/**
 * @author Marko van Dooren
 */
public interface Variable extends ElementWithModifiers, DeclarationWithType {

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



  public LocalLookupContext<?> targetContext() throws LookupException;


  public Variable selectionDeclaration();
}
