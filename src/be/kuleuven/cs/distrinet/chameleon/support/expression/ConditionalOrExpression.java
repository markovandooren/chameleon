package be.kuleuven.cs.distrinet.chameleon.support.expression;

import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;


/**
 * @author Marko van Dooren
 */
public class ConditionalOrExpression extends ConditionalBooleanExpression {

  /**
   * @param first
   * @param second
   */
  public ConditionalOrExpression(Expression first, Expression second) {
    super(first, second);
  }
  
  public ConditionalOrExpression clone() {
    Expression first = getFirst().clone();
    Expression second = getSecond().clone();
    return new ConditionalOrExpression(first, second);
  }

}
