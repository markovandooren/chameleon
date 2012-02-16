package chameleon.support.expression;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.reference.CrossReferenceTarget;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.oo.expression.Expression;
import chameleon.oo.type.Type;
import chameleon.util.Util;

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

  protected Type actualType() throws LookupException {
  	//GENERALIZE PROMOTIONS
    Type firstType = getFirst().getType();
    Type secondType = getSecond().getType();
    if (firstType.equals(secondType)) {
      return firstType;
    }
    
    //TODO I think this code is redundant since NullType is assignable to anything.
    
//    else if (firstType instanceof NullType) {
//      return secondType;
//    }
//    else if (secondType instanceof NullType) {
//      return firstType;
//    }
    
    else if (firstType.assignableTo(secondType)) {
      return secondType;
    }
    else if (secondType.assignableTo(firstType)) {
      return firstType;
    }
//    else if ((firstType.getName().equals("short") && secondType.getName().equals("byte")) || (firstType.getName().equals("byte") && secondType.getName().equals("short"))) {
//      return getNamespace().getDefaultNamespace().findType("short");
//    }

    else {
      throw new ChameleonProgrammerException("Numerical promotion in conditional expression not yet complete");
    }
  }

  public ConditionalExpression clone() {
    return new ConditionalExpression(getCondition().clone(), getFirst().clone(), getSecond().clone());
  }

  public List<Element> children() {
    List<Element> result = Util.createNonNullList(getCondition());
    Util.addNonNull(getFirst(), result);
    Util.addNonNull(getSecond(), result);
    return result;
  }

  public Set getDirectExceptions() throws LookupException {
    return new HashSet();
  }

}
