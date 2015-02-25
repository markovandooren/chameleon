package org.aikodi.chameleon.support.expression;

import java.util.HashSet;
import java.util.Set;

import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.oo.expression.Expression;
import org.aikodi.chameleon.oo.type.Type;

/**
 * @author Marko van Dooren
 */
public class ConditionalExpression extends TernaryExpression {

  /**
   * @param first
   * @param second
   */
  public ConditionalExpression(Expression condition, Expression first, Expression second) {
    super(first, second,condition);
  }

  public Expression getCondition() {
  	return getThird();
  }
  
  public void setCondition(Expression condition) {
  	setThird(condition);
  }

  @Override
protected Type actualType() throws LookupException {
  	//GENERALIZE PROMOTIONS
  	Type result = basicType();
  	if(result == null) {
			  throw new ChameleonProgrammerException("Numerical promotion in conditional expression not yet complete");
  	}
  	return result;
  }

  protected Type basicType() throws LookupException {
  	Type firstType = getFirst().getType();
  	Type secondType = getSecond().getType();
  	if (firstType.equals(secondType)) {
  		return firstType;
  	}
  	else if (firstType.assignableTo(secondType)) {
  		return secondType;
  	}
  	else if (secondType.assignableTo(firstType)) {
  		return firstType;
  	} 
  	return null;
  }

  @Override
protected ConditionalExpression cloneSelf() {
    return new ConditionalExpression(null, null,null);
  }

  public Set getDirectExceptions() throws LookupException {
    return new HashSet();
  }

}
