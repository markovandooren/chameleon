package be.kuleuven.cs.distrinet.chameleon.support.expression;

import java.util.HashSet;
import java.util.Set;

import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;
import be.kuleuven.cs.distrinet.chameleon.oo.language.ObjectOrientedLanguage;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;


/**
 * @author Marko van Dooren
 */
public abstract class ConditionalBooleanExpression extends BinaryExpression {

  /**
   * @param first
   * @param second
   */
  public ConditionalBooleanExpression(Expression first, Expression second) {
    super(first, second);
  }
  
  protected Type actualType() throws LookupException {
    return language(ObjectOrientedLanguage.class).booleanType(view().namespace()); 
  }
  
  public Set<Type> getDirectExceptions() throws LookupException {
    return new HashSet<Type>();
  }
  
}
