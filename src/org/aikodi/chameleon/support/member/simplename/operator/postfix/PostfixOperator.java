package org.aikodi.chameleon.support.member.simplename.operator.postfix;

import org.aikodi.chameleon.oo.method.Method;
import org.aikodi.chameleon.oo.method.MethodHeader;
import org.aikodi.chameleon.support.member.simplename.operator.Operator;

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
