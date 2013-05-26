package be.kuleuven.cs.distrinet.chameleon.support.expression;

import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;


/**
 * @author Marko van Dooren
 */
public class ConditionalAndExpression extends ConditionalBooleanExpression {

  /**
   * @param first
   * @param second
   */
  public ConditionalAndExpression(Expression first, Expression second) {
    super(first, second);
  }

  protected ConditionalAndExpression cloneSelf() {
    return new ConditionalAndExpression(null, null);
  }
  
}
