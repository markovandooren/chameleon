package be.kuleuven.cs.distrinet.chameleon.support.expression;

import java.util.HashSet;
import java.util.Set;

import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;
import be.kuleuven.cs.distrinet.chameleon.oo.language.ObjectOrientedLanguage;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;

/**
 * @author Marko van Dooren
 */
public class InstanceofExpression extends ExprTypeRefContainingExpression {

  public InstanceofExpression(Expression expression, TypeReference type) {
	  setExpression(expression);
    setTypeReference(type);
  }

  protected Type actualType() throws LookupException {
    return language(ObjectOrientedLanguage.class).booleanType(view().namespace());
  }

  public InstanceofExpression clone() {
    return new InstanceofExpression(getExpression().clone(), (TypeReference)getTypeReference().clone());
  }

  public Set<Type> getDirectExceptions() throws LookupException {
    return new HashSet<Type>();
  }

}
