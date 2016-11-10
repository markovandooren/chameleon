package org.aikodi.chameleon.core.variable;

import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.expression.Expression;
import org.aikodi.chameleon.oo.type.DeclarationWithType;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeReference;

/**
 * An interface for variables.
 * 
 * @author Marko van Dooren
 */
public interface Variable extends DeclarationWithType {

  /**
   * @return The initialization expression of this variable.
   */
	public Expression getInitialization();

	/**
	 * Set the initialization expression of this variable.
	 * 
	 * @param expression The new initialization expression of this variable.
	 */
	public void setInitialization(Expression expression);
  
  public TypeReference getTypeReference();
  
  public void setTypeReference(TypeReference ref);

  /**
   * @return the type of this variable.
   *
   * @throws LookupException An error occur while looking up the type of 
   *         this variable.
   */
  public Type getType() throws LookupException;


}
