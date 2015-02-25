package org.aikodi.chameleon.support.member.simplename.operator.prefix;

import org.aikodi.chameleon.oo.method.Method;
import org.aikodi.chameleon.oo.method.MethodHeader;
import org.aikodi.chameleon.support.member.simplename.operator.Operator;

public class PrefixOperator extends Operator {
  
  public PrefixOperator(MethodHeader header) {
    super(header);
  }

  @Override
public boolean sameKind(Method other) {
  	return(other instanceof PrefixOperator);
  }

  @Override
protected PrefixOperator cloneSelf() {
    return new PrefixOperator(null);
  }

}
