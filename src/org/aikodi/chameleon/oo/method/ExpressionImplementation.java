package org.aikodi.chameleon.oo.method;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.expression.Expression;
import org.aikodi.chameleon.oo.statement.Block;
import org.aikodi.chameleon.util.association.Single;

/**
 * An implementation that consists of an expression
 * 
 * @author Marko van Dooren
 */
public class ExpressionImplementation extends Implementation {

  private Single<Expression> _expression = new Single<>(this,true);

  /**
   * Create a new expression implementation with the given expression.
   * @param expression
   */
  public ExpressionImplementation(Expression expression) {
    setExpression(expression);
  }
  
  @Override
  protected Element cloneSelf() {
    return new ExpressionImplementation(null);
  }
  
  /**
   * Set the expression of this implementation to the given expression.
   * 
   * @param expression The new expression of this implementation.
   */
  public void setExpression(Expression expression) {
    set(_expression,expression);
  }

  /**
   * @return The expression of this implementation.
   */
  public Expression expression() {
    return _expression.getOtherEnd();
  }

  @Override
  public boolean compatible() throws LookupException {
    return true;
  }

  @Override
  public Block getBody() {
    return null;
  }

  @Override
  public boolean complete() {
    return expression() != null;
  }
}
