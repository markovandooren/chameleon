package be.kuleuven.cs.distrinet.chameleon.support.member.simplename.operator.prefix;

import be.kuleuven.cs.distrinet.chameleon.oo.method.Method;
import be.kuleuven.cs.distrinet.chameleon.oo.method.MethodHeader;
import be.kuleuven.cs.distrinet.chameleon.support.member.simplename.operator.Operator;

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
