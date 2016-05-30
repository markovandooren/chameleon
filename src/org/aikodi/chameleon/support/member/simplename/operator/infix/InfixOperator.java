package org.aikodi.chameleon.support.member.simplename.operator.infix;

import org.aikodi.chameleon.oo.method.Method;
import org.aikodi.chameleon.oo.method.MethodHeader;
import org.aikodi.chameleon.support.member.simplename.operator.Operator;

/**
 * @author Marko van Dooren
 */
public class InfixOperator extends Operator {

  public InfixOperator(MethodHeader header) {
    super(header);
  }

  @Override
protected InfixOperator cloneSelf() {
    return new InfixOperator(null);
  }

  @Override
public boolean sameKind(Method other) {
	  return(other instanceof InfixOperator);
  }

}
