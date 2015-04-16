package org.aikodi.chameleon.oo.expression;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.util.association.Single;

/**
 * An expression that wraps another expression in parentheses.
 * 
 * @author Marko van Dooren
 */
public class ParExpression extends Expression {

  private Single<Expression> _expression = new Single<>(this);

  
  /**
   * Create a new parentheses expression that wraps the given expression
   * 
   * @param expression The expression to be wrapped.
   */
  public ParExpression(Expression expression) {
    setExpression(expression);
  }

  /**
   * @return The expression wrapped by this parentheses expression.
   */
  public Expression expression() {
    return _expression.getOtherEnd();
  }
  
  /**
   * Set the expression wrapped by this parentheses expression.
   * 
   * @param expression The expression wrapped by this parentheses expression.
   */
  public void setExpression(Expression expression) {
    set(_expression,expression);
  }

  /**
   * {@inheritDoc}
   * 
   * The actual type of a parentheses expression is the type of the
   * wrapped expression.
   */
  @Override
  protected Type actualType() throws LookupException {
    return expression().getType();
  }

  @Override
  protected Element cloneSelf() {
    return new ParExpression(null);
  }
}
