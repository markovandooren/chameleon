package chameleon.support.expression;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.oo.expression.Expression;
import chameleon.oo.language.ObjectOrientedLanguage;
import chameleon.oo.type.Type;


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
    return language(ObjectOrientedLanguage.class).booleanType(); 
  }
  
  public Set<Type> getDirectExceptions() throws LookupException {
    return new HashSet<Type>();
  }
  
}
