package org.aikodi.chameleon.core.variable;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.Name;
import org.aikodi.chameleon.core.lookup.LocalLookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.modifier.ElementWithModifiers;
import org.aikodi.chameleon.oo.expression.Expression;
import org.aikodi.chameleon.oo.type.DeclarationWithType;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeReference;

/**
 * @author Marko van Dooren
 */
public interface Variable extends ElementWithModifiers, DeclarationWithType {

	public Expression getInitialization();

	public void setInitialization(Expression expr);
  
  /**
   * Return the signature of this member.
   */
  @Override
public Name signature();
  
  public abstract TypeReference getTypeReference();
  
  public abstract void setTypeReference(TypeReference ref);

  public Type getType() throws LookupException;



  @Override
public LocalLookupContext<?> targetContext() throws LookupException;


  @Override
public Declaration selectionDeclaration() throws LookupException;
}
