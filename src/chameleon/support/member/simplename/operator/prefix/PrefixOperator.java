package chameleon.support.member.simplename.operator.prefix;

import chameleon.oo.method.Method;
import chameleon.oo.method.MethodHeader;
import chameleon.support.member.simplename.operator.Operator;

public class PrefixOperator extends Operator {
  
  public PrefixOperator(MethodHeader header) {
    super(header);
  }

  public boolean sameKind(Method other) {
  	return(other instanceof PrefixOperator);
  }

  protected PrefixOperator cloneThis() {
    return new PrefixOperator((MethodHeader) header().clone());
  }

}
