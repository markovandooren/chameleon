package chameleon.support.member.simplename.operator.postfix;

import chameleon.oo.method.Method;
import chameleon.oo.method.MethodHeader;
import chameleon.support.member.simplename.operator.Operator;

/**
 * @author Marko van Dooren
 */
public class PostfixOperator extends Operator {

  public PostfixOperator(MethodHeader header) {
    super(header);
  }
  
  public boolean sameKind(Method other) {
	  	return(other instanceof PostfixOperator);
	  }  

  protected PostfixOperator cloneThis() {
    return new PostfixOperator((MethodHeader) header().clone());
  }

}
