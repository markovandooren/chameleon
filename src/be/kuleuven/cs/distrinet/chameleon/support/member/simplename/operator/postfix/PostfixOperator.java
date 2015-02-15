package be.kuleuven.cs.distrinet.chameleon.support.member.simplename.operator.postfix;

import be.kuleuven.cs.distrinet.chameleon.oo.method.Method;
import be.kuleuven.cs.distrinet.chameleon.oo.method.MethodHeader;
import be.kuleuven.cs.distrinet.chameleon.support.member.simplename.operator.Operator;

/**
 * @author Marko van Dooren
 */
public class PostfixOperator extends Operator {

  public PostfixOperator(MethodHeader header) {
    super(header);
  }
  
  @Override
public boolean sameKind(Method other) {
	  	return(other instanceof PostfixOperator);
	  }  

  @Override
protected PostfixOperator cloneSelf() {
    return new PostfixOperator(null);
  }

}
