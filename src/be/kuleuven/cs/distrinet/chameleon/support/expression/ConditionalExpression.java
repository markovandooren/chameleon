package be.kuleuven.cs.distrinet.chameleon.support.expression;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableList;

import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;
import be.kuleuven.cs.distrinet.chameleon.oo.language.ObjectOrientedLanguage;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;

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
    } else {
			ObjectOrientedLanguage language = language(ObjectOrientedLanguage.class);
			if (firstType.isTrue(language.REFERENCE_TYPE) && secondType.isTrue(language.REFERENCE_TYPE)) {
				TypeReference first = language.reference(firstType);
				TypeReference second = language.reference(secondType);
				return language.subtypeRelation().leastUpperBound(ImmutableList.of(first,second));
			}
			else {
			  throw new ChameleonProgrammerException("Numerical promotion in conditional expression not yet complete");
			}
		}
//  else if ((firstType.getName().equals("short") && secondType.getName().equals("byte")) || (firstType.getName().equals("byte") && secondType.getName().equals("short"))) {
//  return getNamespace().getDefaultNamespace().findType("short");
//}
  }

  protected ConditionalExpression cloneSelf() {
    return new ConditionalExpression(null, null,null);
  }

  public Set getDirectExceptions() throws LookupException {
    return new HashSet();
  }

}
